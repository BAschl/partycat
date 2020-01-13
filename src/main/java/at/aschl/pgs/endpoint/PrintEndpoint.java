package at.aschl.pgs.endpoint;

import at.aschl.pgs.data.Image;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Controller
public class PrintEndpoint {

  private final SimpMessageSendingOperations messagingTemplate;

  public PrintEndpoint(
      SimpMessageSendingOperations messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping("/printEndpoint/socket")
  public void print(Image image) throws IOException {
    FileOutputStream fos = new FileOutputStream(image.getFilename());
    fos.write(image.getImgdata());
    throw new IOException();

  }

  @MessageMapping("/printEndpoint2/socket")
  public void print2(byte[] image) throws IOException {
    FileOutputStream fos = new FileOutputStream("img.jpg");
    fos.write(image);
  }

  @Bean
  public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
    container.setMaxTextMessageBufferSize(9999999);
    container.setMaxBinaryMessageBufferSize(9999999);
    System.out.println("Websocket factory returned");
    return container;
  }
//  @MessageMapping("/print")
//  public void print(byte[] image) throws IOException {
//      FileOutputStream fos = new FileOutputStream("img.jpg");
//      fos.write(image);
//  }
}
