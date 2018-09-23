package mibs.asterisk.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
@SpringBootApplication
public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
    	System.out.println("Run me...");
        SpringApplication.run(App.class, args);
        
       
    }
}
