package at.aschl.pgs.endpoint;

import at.aschl.pgs.data.Message;
import at.aschl.pgs.data.Room;
import java.util.HashMap;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class RoomEndpoint {

  private final SimpMessageSendingOperations messagingTemplate;
  private final SecretHitlerEndpoint secretHitlerEndpoint;

  private Map<String, Room> rooms = new HashMap<>();

  public RoomEndpoint(
      SimpMessageSendingOperations messagingTemplate,
      SecretHitlerEndpoint secretHitlerEndpoint) {
    this.messagingTemplate = messagingTemplate;
    this.secretHitlerEndpoint = secretHitlerEndpoint;
  }

  @MessageMapping("/createRoom")
  @SendTo("/topic/roomCreated")
  public String createRoom() throws Exception {
    Room room = null;
    do {
      room = new Room();
    } while (rooms.putIfAbsent(room.getRoomCode(), room) == null);
    System.out.println("Creating room with roomcode " + room.getRoomCode());
    return room.getRoomCode();
  }

  @MessageMapping("/joinRoom")
  @SendTo("/topic/roomJoined")
  public void joinRoom(Message message) throws Exception {
    System.out
        .println("join room " + message.getRoomCode() + " with username " + message.getUserName());
    if (rooms.containsKey(message.getRoomCode())) {
      Room room = rooms.get(message.getRoomCode());
      room.getUsers().add(HtmlUtils.htmlEscape(message.getUserName()));

      messagingTemplate.convertAndSend("/topic/" + message.getRoomCode() + "/roomJoined", room);

    } else {
      throw new Exception("Room with code " + message.getRoomCode() + " not found");
    }
  }

  @MessageMapping("/startGame")
  public void startGame(Message message) {
    var roomCode = message.getRoomCode();
    if (rooms.containsKey(roomCode) && rooms.get(roomCode).getUsers().get(0)
        .equals(message.getUserName())) {
      secretHitlerEndpoint.startGame(rooms.get(roomCode));
    }
  }
}
