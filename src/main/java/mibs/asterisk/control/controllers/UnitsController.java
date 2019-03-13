package mibs.asterisk.control.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


import mibs.asterisk.control.config.AppConfig;
import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.Configuration;
import mibs.asterisk.control.dao.Equipments;
import mibs.asterisk.control.dao.Pbx;
import mibs.asterisk.control.dao.Units;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.entity.EquipmentsEntity;
import mibs.asterisk.control.entity.UnitsEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.exception.CopyConfigException;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.repository.EquipmentsRepository;
import mibs.asterisk.control.repository.UnitsRepository;
import mibs.asterisk.control.service.AsteriskQuery;
import mibs.asterisk.control.service.AsteriskResponce;
import mibs.asterisk.control.service.UsersDetails;
import mibs.asterisk.control.utils.FSContainer;
import mibs.asterisk.control.utils.PNameQ;

@Controller
public class UnitsController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(UnitsController.class);
	
	static final String PEERS_LINE="^Output:\\s+\\w+";
	final Pattern pattern = Pattern.compile(PEERS_LINE);

	static final String SIP_RELOAD="sip reload";
	
	static final String SIP_SHOW_PEERS="sip show peers";
	
	private FSContainer rootFS;
	private Connection connect = null;

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private UnitsRepository unitsRepository;
	@Autowired
	private EquipmentsRepository equipmentsRepository;
	@Autowired
	private ConfigurationRepository configurationRepository;

	private Map<String, String> mp = new TreeMap<>();;
	
	private BiConsumer<String, List<String>> handleShowPeers = (line, result) -> {
		
		String ln = line + "\n";
		Matcher m =  Pattern.compile(PEERS_LINE).matcher(ln);
		if (m.find()) {
			
			 String peer = m.group(0).split(":")[1];
			 String s = mp.get(peer) !=null ? peer + " " +  mp.get(peer): peer;
			 result.add(s);
		}
//		if (m.matches()) {
//			String peer = ln.split(" ")[0].split("/")[0];
//			String s = mp.get(peer) !=null ? peer + " " +  mp.get(peer): peer;
//			result.append(s + "\n");
//		}
	};
	private BiConsumer<String, StringBuilder> handleSipReload = (line, result) -> {
		//result.append(line + "\n");
		if (line.contains("ActionID:12345")) result.append("SIP RELOADED\n");
	};
	
	Map<String, BiConsumer<String, StringBuilder>> cmd;
	
	public UnitsController() {
		// cmd = new TreeMap<>();
		// cmd.put(SIP_RELOAD, handleSipReload);
		// cmd.put(SIP_SHOW_PEERS, handleShowPeers);
	}
	
	@MessageMapping("/receiver")
	@SendTo("/topic/sender")
	public AsteriskResponce handleMessage(AsteriskQuery query) throws Exception {
		
		if (query.getCommand().equals("sip reload")){
			return handleSipReload( query );
		}else {
			return handleSipShowPeers( query );
		}
	}
	
	private AsteriskResponce handleSipReload(AsteriskQuery query) {
		 Optional<ConfigurationEntity> opt = configurationRepository.findById(Long.valueOf(query.getId()));
		if (!opt.isPresent()) return  new AsteriskResponce("ERROR_ASTERISK_NOT_FOUND");
		ConfigurationEntity config = opt.get();
		String host = config.getAsthost();
		int port = Integer.parseInt(appConfig.getAmi_port());
		String user = config.getAstuser();
		String password = config.getAstpassword();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
//			socket.setSoTimeout(15000);
			OutputStream out = socket.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			writer = new BufferedWriter(writer);
			writer.write("Action: Login\r\nActionID:12345\r\nUsername: " + user + "\r\nSecret: " + password + "\r\n\r\n");
			writer.flush();
			InputStream inp = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inp, "UTF-8"));
		
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.contains(" Authentication failed")) {
					return new AsteriskResponce("ERROR_ASTERISK_CONNECTION");
				}
				if (line.contains("Response: Success")) {
					 return new AsteriskResponce("SIP RELOADED");
				}
				if (line.contains("Authentication accepted")) {
					writer.write("Action: COMMAND\r\nActionID:12345\r\ncommand: sip reload\r\n\r\n");
					writer.flush();
				}
			}
			writer.write("Action: Logoff\r\nActionID:12345\r\n\r\n");
			writer.flush();
			return new AsteriskResponce("ERROR SIP RELOADED");
		} catch (IOException e) {
			logger.error(e.getMessage());
			return new AsteriskResponce("ERROR_ASTERISK_CONNECTION");
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	
	}
	private String findPeer(String phone) {
		String result = phone;
		for(int  i =0; i < rootFS.getContainers().size(); i++) {
			FSContainer fc = rootFS.getContainers().get(i);
			for (int j=0; j < fc.getContainers().size(); j++ ) {
				FSContainer ffc =  fc.getContainers().get(j);
				for(int k = 0; k < ffc.getPNameQ().getEquipments().size(); k++) {
					Equipments equioment =  ffc.getPNameQ().getEquipments().get(k);
					if (phone.trim().contains( equioment.getPhone().trim())) {
						result +=  " -> " + ffc.getPNameQ().getName() + " -> " + fc.getPNameQ().getName();
						break;
					}
				}
			}
		}
		return result;
	}
	private String findPeerCoinsidence(List<String> input) {
		StringBuilder result = new StringBuilder();
		for(int i=0; i < input.size(); i++) {
		    result.append(findPeer(input.get(i)) + "\n");
		}
		return result.toString();
		
	}
	private AsteriskResponce handleSipShowPeers(AsteriskQuery query) {
	
		Optional<ConfigurationEntity> opt = configurationRepository.findById(Long.valueOf(query.getId()));
		if (!opt.isPresent()) return  new AsteriskResponce("ERROR_ASTERISK_NOT_FOUND");
		ConfigurationEntity config = opt.get();
		String host = config.getAsthost();
		int port = Integer.parseInt(appConfig.getAmi_port());

		String user = config.getAstuser();
		String password = config.getAstpassword();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			socket.setSoTimeout(15000);
			OutputStream out = socket.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			writer = new BufferedWriter(writer);
			writer.write("Action: Login\r\nActionID:12345\r\nUsername: " + user + "\r\nSecret: " + password + "\r\n\r\n");
			writer.flush();
			InputStream inp = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inp, "UTF-8"));
			List<String> result = new ArrayList<>();
			boolean flag = false;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.contains(" Authentication failed")) {
					return new AsteriskResponce("ERROR_ASTERISK_CONNECTION");
				}
				if (line.contains("Response: Success") & !flag) {
					flag = true;
				}
				if (flag) handleShowPeers.accept(line, result);
				
				if (line.contains("Authentication accepted")) {
					writer.write("Action: COMMAND\r\nActionID:12345\r\ncommand: " + query.getCommand() + "\r\n\r\n");
					writer.flush();
				}
				if (line.contains("sip peers [Monitored:")) {
					break;
				}
			
			}
			writer.write("Action: Logoff\r\nActionID:12345\r\n\r\n");
			writer.flush();
			return new AsteriskResponce( findPeerCoinsidence(result));
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
			return new AsteriskResponce("ERROR_ASTERISK_CONNECTION");
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	@RequestMapping(value = { "/sendFileToAsterisk" }, method = { RequestMethod.POST })
	public @ResponseBody String sendFileToAsterisk(@RequestBody Pbx pbx) {

		int result = -1;
	
		ExecutorService service = null;
		try {
			Optional<ConfigurationEntity> opt = configurationRepository.findById(pbx.getId());
			if (!opt.isPresent())
				return "ERROR_FILE_SENDING";
			ConfigurationEntity conf = opt.get();
			service = Executors.newSingleThreadExecutor();
			Future<Integer> future = service.submit(() -> {
				return copySIPConfig(
									conf.getSshlogin(), 
									conf.getAsthost(), 
									Integer.parseInt(appConfig.getSshport()), 
									conf.getSshpassword(), 
									appConfig.getKnown_hosts(),
									appConfig.getSipconfig(),
									appConfig.getSipcongigremote()
									);
				
			});
			result = future.get();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "ERROR_FILE_SENDING :" + e.getMessage();
		}finally {
			if (service != null)
				service.shutdown();
		}
		return (result == -1) ? "ERROR_FILE_SENDING" : "SUCCESS_FILE_SENDING";
	}

	@RequestMapping(value = { "/createSipConf" }, method = { RequestMethod.POST })
	public @ResponseBody String createSipConf() {

		LocalDateTime ld = LocalDateTime.now();
		final StringBuilder sb = new StringBuilder();
		List<EquipmentsEntity> entity = equipmentsRepository.findAll();
		sb.append(appConfig.getConfigheader() + "\n");
		sb.append("; Asterisk Control sip.cong file, created at " + ld.getDayOfMonth() + "-" + ld.getMonth() + "-"
				+ ld.getYear() + ":" + ld.getHour() + ":" + ld.getMinute() + ":" + ld.getSecond() + "\n");
		entity.forEach(s -> {
			sb.append("[" + s.getPhone() + "](" + appConfig.getSiptemplate() + ")\n");
			sb.append("permit=" + s.getIpaddress() + "/" + s.getNetmask() + "\n");
			sb.append("secret=" + s.getPassword() + "\n");
			sb.append("callerid=" + s.getPhone() + "\n\n");
		});
		try {
			Files.write(Paths.get(appConfig.getSipconfig()), sb.toString().getBytes());
			return sb.toString();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return "ERROR_CREATE_CONFIG_FILE";
		}
	}

	@RequestMapping(value = { "/dropUnit" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult dropUnit(@RequestBody Units un) {

		if (un.getP() == null)
			return new ActionResult("UNIT_NOT_DROPED");
		Optional<UnitsEntity> unitOps = unitsRepository.findById(un.getP());

		if (!unitOps.isPresent())
			return new ActionResult("UNIT_NOT_DROPED");
		List<UnitsEntity> units = unitsRepository.findByQ(unitOps.get().getId());
		if (units.size() > 0)
			return new ActionResult("UNIT_NOT_DROPED");
		try {
			unitsRepository.delete(unitOps.get());
			return new ActionResult("UNIT_DROPED");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ActionResult("UNIT_NOT_DROPED");
		}
	}

	@RequestMapping(value = { "/dropCenter" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult dropCenter(@RequestBody Units un) {

		if (un.getP() == null)
			return new ActionResult("UNIT_NOT_DROPED");
		List<EquipmentsEntity> equipmentOps = equipmentsRepository.findByP(un.getP());
		if (equipmentOps.size() > 0)
			return new ActionResult("UNIT_NOT_DROPED");
		Optional<UnitsEntity> unit = unitsRepository.findById(un.getP());
		if (!unit.isPresent())
			return new ActionResult("UNIT_NOT_DROPED");
		try {
			unitsRepository.delete(unit.get());
			return new ActionResult("UNIT_DROPED");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ActionResult("UNIT_NOT_DROPED");
		}
	}

	@RequestMapping(value = { "/findUnit" }, method = { RequestMethod.GET })
	public @ResponseBody Units findUnit(@RequestParam(value = "id", required = true) Long id) {
		final Units unit = new Units();
		Optional<UnitsEntity> entity = unitsRepository.findById(id);
		entity.ifPresent(en -> {
			unit.setP(en.getId());
			unit.setName(en.getUnit());
			unit.setQ(en.getQ());
		});
		return unit;
	}

	@RequestMapping(value = { "/findEquipment" }, method = { RequestMethod.GET })
	public @ResponseBody Equipments findEquipment(@RequestParam(value = "id", required = true) Long id) {
		final Equipments equipment = new Equipments();
		Optional<EquipmentsEntity> entity = equipmentsRepository.findById(id);
		entity.ifPresent(en -> {
			equipment.setId(en.getId());
			equipment.setOffice(en.getOffice());
			equipment.setPhone(en.getPhone());
			equipment.setIpaddress(en.getIpaddress());
			equipment.setNetmask(en.getNetmask());
			equipment.setGateway(en.getGateway());
			equipment.setP(en.getP());
			equipment.setPassword(en.getPassword());
			equipment.setPerson(en.getPerson());
			equipment.setRecordIn(en.getRecordIn());
			equipment.setRecordOut(en.getRecordOut());
			equipment.setExternal(en.getExternal());
			equipment.setTemplateid(en.getTemplate().getId());
			equipment.setTemplatename(en.getTemplate().getName());
			
		});
		return equipment;
	}

	@RequestMapping(value = { "/saveUnit" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult saveUnit(@RequestBody Units un) {
		if (un.getName().length() == 0)
			return new ActionResult("UNIT_NOT_SAVED");

		if (un.getP() != null) {
			try {
				unitsRepository.updateUnit(un.getName(), un.getP());
				return new ActionResult("UNIT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("UNIT_NOT_SAVED");
			}
		} else {
			UnitsEntity unit = new UnitsEntity();
			unit.setUnit(un.getName());
			unit.setQ(Long.valueOf(0));
			unit.setPbx(un.getPbx());
			try {
				unitsRepository.save(unit);
				return new ActionResult("UNIT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("UNIT_NOT_SAVED");
			}
		}
	}

	@RequestMapping(value = { "/dropEquipment" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult dropEquipment(@RequestBody Equipments equipment) {
		if (equipment.getId() == null)
			return new ActionResult("EQUIPMENT_NOT_DROPED");
		Optional<EquipmentsEntity> eqOpt = equipmentsRepository.findById(equipment.getId());
		if (!eqOpt.isPresent())
			return new ActionResult("EQUIPMENT_NOT_DROPED");
		try {
			equipmentsRepository.delete(eqOpt.get());
			return new ActionResult("EQUIPMENT_DROPED");
		} catch (Exception e) {
			return new ActionResult("EQUIPMENT_NOT_DROPED");
		}
	}

	@RequestMapping(value = { "/saveEquipment" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult saveEquipment(@RequestBody Equipments eq) {
		
		eq.setExternal( eq.getExternal().matches("\\d{7,}+") ? eq.getExternal() : "No" );
		
		
		if (eq.getId() != null) {
			try {
				equipmentsRepository.updateEquipments(eq.getPhone(), eq.getPassword(), eq.getOffice(), eq.getP(),
						eq.getIpaddress(), eq.getNetmask(), eq.getGateway(), eq.getPerson(),eq.getRecordIn(), eq.getRecordOut(), eq.getExternal(), eq.getId());
				return new ActionResult("EQUIPMENT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("EQUIPMENT_NOT_SAVED");
			}
		} else {
			EquipmentsEntity equipment = new EquipmentsEntity();
			equipment.setPhone(eq.getPhone());
			equipment.setOffice(eq.getOffice());
			equipment.setIpaddress(eq.getIpaddress());
			equipment.setNetmask(eq.getNetmask());
			equipment.setGateway(eq.getGateway());
			equipment.setP(eq.getP());
			equipment.setPassword(eq.getPassword());
			equipment.setPerson(eq.getPerson());
			equipment.setRecordIn(eq.getRecordIn());
			equipment.setRecordOut(eq.getRecordOut());
			equipment.setExternal(eq.getExternal());
			
			try {
				equipmentsRepository.save(equipment);
				return new ActionResult("EQUIPMENT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("EQUIPMENT_NOT_SAVED");
			}
		}
	}

	@RequestMapping(value = { "/saveCenter" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult saveCenter(@RequestBody Units un) {
		if (un.getName().length() == 0)
			return new ActionResult("UNIT_NOT_SAVED");

		if (un.getQ() != null) {
			try {
				unitsRepository.updateUnit(un.getName(), un.getP());
				return new ActionResult("UNIT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("UNIT_NOT_SAVED");
			}
		} else {
			UnitsEntity unit = new UnitsEntity();
			unit.setUnit(un.getName());
			unit.setQ(un.getP());
			unit.setPbx(un.getPbx());
			try {
				unitsRepository.save(unit);
				return new ActionResult("UNIT_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("UNIT_NOT_SAVED");
			}
		}
	}

	@RequestMapping(value = { "/showAllUnits" }, method = { RequestMethod.GET })
	public @ResponseBody FSContainer showFSContainer(@RequestParam(value = "pbx", required = true) Long pbx) {
		mp.clear();
		rootFS = new FSContainer(new PNameQ(0, "ROOT", 0));
		try {
			connect = DriverManager.getConnection(appConfig.getDatasourceUrl(), appConfig.getUsername(), appConfig.getPassword());
			fillSQL(Long.valueOf(0), pbx, rootFS);
			
		
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	
		return rootFS;

	}

	private void fill(Long p, FSContainer fs) {
		List<UnitsEntity> resultSet = unitsRepository.findByQ(new Long(p));
		resultSet.forEach(rs -> {
			Long p1 = rs.getId();
			FSContainer tempFs = new FSContainer(new PNameQ(p1, rs.getUnit(), rs.getQ()));
			fs.addContainer(tempFs);
			fill(p1, tempFs);
		});
	}

	private void fillSQL(Long p, Long pbx, FSContainer fs) throws SQLException {
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement
				.executeQuery("select * from units where q=" + p + " and pbx=" + pbx + " order by unit");
		while (resultSet.next()) {
			long p1 = resultSet.getLong("p");
			long q = resultSet.getLong("q");
			String unit = resultSet.getString("unit");

			PNameQ pnameq = new PNameQ(p1, unit, q);
			List<EquipmentsEntity> eqs = equipmentsRepository.findByP(p1);
			
			if (eqs != null && eqs.size() > 0) {
				eqs.forEach(e -> {
					Equipments equipment = new Equipments();
					equipment.setId(e.getId());
					equipment.setPhone(e.getPhone());
					equipment.setIpaddress(e.getIpaddress());
					equipment.setOffice(e.getOffice());
					equipment.setNetmask(e.getNetmask());
					equipment.setGateway(e.getGateway());
					equipment.setP(e.getP());
					equipment.setPerson(e.getPerson());
					equipment.setPassword(e.getPassword());
					equipment.setRecordIn(e.getRecordIn());
					equipment.setRecordOut(e.getRecordOut());
					equipment.setExternal(e.getExternal());
					equipment.setTemplateid(e.getTemplate().getId());
					equipment.setTemplatename(e.getTemplate().getName());

					pnameq.addEquipments(equipment);
					mp.put(e.getPhone(), " - " + fs.getPNameQ().getName() + " - " +  pnameq.getName() );
				});
			}

			FSContainer tempFs = new FSContainer(pnameq);

			fs.addContainer(tempFs);
			fillSQL(p1, pbx, tempFs);
		}
		statement.close();
	}
}
