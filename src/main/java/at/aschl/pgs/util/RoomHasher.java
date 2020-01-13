package at.aschl.pgs.util;

public class RoomHasher {

  private static final int POSSIBILITIES = 456976;
  private static final int PRIM = 456979;

  public static String hash(long nr) {
    nr = (((nr << 5) + nr) + nr)%PRIM;
    String hash = "";
    while(nr>0){
      hash += (char)('A'+(nr%26));
      nr/=26;
    }
    return hash;
  }

}
