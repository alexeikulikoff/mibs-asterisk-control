package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.Configuration;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.entity.UnitsEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.repository.UnitsRepository;

@Controller
public class ConfigController extends AbstractController {

	static Logger logger = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	private ConfigurationRepository configurationRepository;
	@Autowired
	private UnitsRepository unitsRepository;

	@RequestMapping(value = { "/findConfig" }, method = { RequestMethod.GET })
	public @ResponseBody Configuration findConfig(@RequestParam(value = "id", required = true) Long id) {
		final Configuration config = new Configuration();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(id);
		entity.ifPresent(en -> {
			config.setId(en.getId());
			config.setAstname(en.getAstname());
			config.setDbhost(en.getDbhost());
			config.setDbname(en.getDbname());
			config.setDbuser(en.getDbuser());
			config.setDbpassword(en.getDbpassword());
			config.setSshlogin(en.getSshlogin());
			config.setSshpassword(en.getSshpassword());
			config.setAsthost(en.getAsthost());
			config.setAstuser(en.getAstuser());
			config.setAstpassword(en.getAstpassword());
		});
		return config;
	}

	@RequestMapping(value = { "/saveConfig" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult saveConfig(@RequestBody Configuration conf) {

		if (conf.getId() != null) {
			try {
				configurationRepository.updateConfiguration(conf.getAstname(), conf.getDbhost(), conf.getDbname(),
						conf.getDbuser(), conf.getDbpassword(), conf.getSshlogin(), conf.getSshpassword(),
						conf.getAsthost(), conf.getAstuser(), conf.getAstpassword(), conf.getId());
				return new ActionResult("CONFIG_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("CONFIG_NOT_SAVED");
			}
		} else {
			ConfigurationEntity configuration = new ConfigurationEntity();
			configuration.setAstname(conf.getAstname());
			configuration.setDbhost(conf.getDbhost());
			configuration.setDbname(conf.getDbname());
			configuration.setDbuser(conf.getDbuser());
			configuration.setDbpassword(conf.getDbpassword());
			configuration.setSshlogin(conf.getSshlogin());
			configuration.setSshpassword(conf.getSshpassword());
			configuration.setAsthost(conf.getAsthost());
			configuration.setAstuser(conf.getAstuser());
			configuration.setAstpassword(conf.getAstpassword());
			try {
				configurationRepository.save(configuration);
				return new ActionResult("CONFIG_SAVED");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ActionResult("CONFIG_NOT_SAVED");
			}
		}

	}

	@RequestMapping(value = { "/findAllconfig" }, method = { RequestMethod.GET })
	public @ResponseBody List<Configuration> findAllConfig(Model model) {
		final List<Configuration> configs = new ArrayList<>();
		List<ConfigurationEntity> entity = configurationRepository.findAll();
		if (entity != null && entity.size() > 0) {
			entity.forEach(en -> {
				Configuration config = new Configuration();
				config.setId(en.getId());
				config.setAstname(en.getAstname());
				config.setDbhost(en.getDbhost());
				config.setDbname(en.getDbname());
				config.setDbuser(en.getDbuser());
				config.setDbpassword(en.getDbpassword());
				config.setSshlogin(en.getSshlogin());
				config.setSshpassword(en.getSshpassword());
				config.setAsthost(en.getAsthost());
				config.setAstuser(en.getAstuser());
				config.setAstpassword(en.getAstpassword());
				configs.add(config);
			});
		}
		return configs;

	}

	@RequestMapping(value = { "/dropConfig" }, method = { RequestMethod.POST })
	public @ResponseBody ActionResult dropConfig(@RequestBody Configuration conf) {
		if (conf.getId() == null)
			return new ActionResult("CONFIG_NOT_DROPED");
		Optional<ConfigurationEntity> opt = configurationRepository.findById(conf.getId());
		if (!opt.isPresent())
			return new ActionResult("CONFIG_NOT_DROPED");
		List<UnitsEntity> units = unitsRepository.findByPbx(conf.getId());
		System.out.println(units);
		System.out.println(units.size());
		if (units != null && units.size() > 0)
			return new ActionResult("CONFIG_NOT_DROPED");
		try {
			configurationRepository.delete(opt.get());
			return new ActionResult("CONFIG_DROPED");
		} catch (Exception e) {
			return new ActionResult("CONFIG_NOT_DROPED");
		}
		
	}
}
