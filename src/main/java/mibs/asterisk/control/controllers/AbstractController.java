package mibs.asterisk.control.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import mibs.asterisk.control.repository.UserRepository;

abstract class AbstractController {
	static Logger logger = LoggerFactory.getLogger(AbstractController.class);
	
	private static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
	private int copySIPConfig(String user, String host, int port, String password, String known_host, String lfile, String rfile) {
		JSch jsch = new JSch();
		try {
			jsch.setKnownHosts(known_host);
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.connect();
			boolean ptimestamp = false;
			FileInputStream fis = null;
			String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile;
			Channel channel = (Channel) session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
		
				OutputStream out = channel.getOutputStream();
				InputStream in = channel.getInputStream();
				channel.connect();
				if (checkAck(in) != 0) {
					// throw new CopyConfigException("Error copying file...");
					// System.exit(0);
					return -1;
				}
				File _lfile = new File(lfile);

				if (ptimestamp) {
					command = "T " + (_lfile.lastModified() / 1000) + " 0";
					// The access time should be sent here,
					// but it is not accessible with JavaAPI ;-<
					command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
					out.write(command.getBytes());
					out.flush();
					if (checkAck(in) != 0) {
						// throw new CopyConfigException("Error copying file...");
						// System.exit(0);
						return -1;
					}
				}

				// send "C0644 filesize filename", where filename should not include '/'
				long filesize = _lfile.length();
				command = "C0644 " + filesize + " ";
				if (lfile.lastIndexOf('/') > 0) {
					command += lfile.substring(lfile.lastIndexOf('/') + 1);
				} else {
					command += lfile;
				}
				command += "\n";
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					// throw new CopyConfigException("Error copying file...");
					// System.exit(0);
					return -1;
				}

				// send a content of lfile
				fis = new FileInputStream(lfile);
				byte[] buf = new byte[1024];
				while (true) {
					int len = fis.read(buf, 0, buf.length);
					if (len <= 0)
						break;
					out.write(buf, 0, len); // out.flush();
				}
				fis.close();
				fis = null;
				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
				if (checkAck(in) != 0) {
					// throw new CopyConfigException("Error copying file...");
					// System.exit(0);
					return -1;
				}
				out.close();
				channel.disconnect();
				session.disconnect();
				return 0;
				// System.exit(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	
}
