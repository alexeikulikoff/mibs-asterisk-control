package mibs.asterisk.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//  openssl pkcs12 -export -out keystore.p12 -inkey commercial.key -in ldc.ru.crt -certfile RapidSSL_CA_2018_bundle.pem -name tomcat

@SpringBootApplication
public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
    
        SpringApplication.run(App.class, args);
        
       
    }
}
