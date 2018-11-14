package mibs.asterisk.control;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Component
@Order(2)
public class AppRunner implements ApplicationRunner {

	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		Optional<ConfigurationEntity> entity = configurationRepository.findById( 1L );
		
	}

}
