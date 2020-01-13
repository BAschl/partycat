package at.aschl.pgs.service.implementation;

import at.aschl.pgs.service.RoomService;
//import com.twitter.hashing.KeyHashers;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class SimpleRoomService implements RoomService {

  static AtomicLong roomId = new AtomicLong();

  @Override
  public String createRoom() {
    return "asdf";// +KeyHashers.MURMUR3().hashKey((""+roomId.getAndAdd(1)).getBytes());
  }
}
