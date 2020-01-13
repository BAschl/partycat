package at.aschl.pgs.service;

/**
 * The interface Room at.aschl.pgs.service.
 */
public interface RoomService {

  /**
   * Create a new room and give a human-readable string back, which users can use to join
   *
   * @return the room code
   */
  String createRoom();

}
