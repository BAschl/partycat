package at.aschl.pgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class PartyGameServiceApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(PartyGameServiceApplication.class, args);
  }
}
