package at.aschl.pgs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.setMessageSizeLimit(1024* 1024 * 1024);
    registration.setSendBufferSizeLimit(1024*1024 * 1024);
    registration.setSendTimeLimit(20000);
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic/");
    config.setApplicationDestinationPrefixes("/app/");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/room").setAllowedOrigins("*").withSockJS();
//    registry.addEndpoint("/printEndpoint").setAllowedOrigins("*").withSockJS();
    registry.addEndpoint("/socket").setAllowedOrigins("*");
//    registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
    registry.addEndpoint("/printEndpoint/websocket").setAllowedOrigins("*");
    registry.addEndpoint("/printEndpoint/socket").setAllowedOrigins("*");
    registry.addEndpoint("/printEndpoint2/websocket").setAllowedOrigins("*");
    registry.addEndpoint("/printEndpoint2/socket").setAllowedOrigins("*");
  }

}