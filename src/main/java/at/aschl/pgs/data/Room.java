package at.aschl.pgs.data;

import at.aschl.pgs.util.RoomHasher;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Room {
  private static AtomicLong roomIdCounter =new AtomicLong(3000);

  private List<String> users;
  private String roomCode;

  public Room() {
    roomCode = RoomHasher.hash(roomIdCounter.addAndGet(1));
    users = new ArrayList<>();
  }

  public String getRoomCode() {
    return roomCode;
  }


  public List<String> getUsers() {
    return users;
  }

  public void setUsers(List<String> users) {
    this.users = users;
  }
}
