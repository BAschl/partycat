package at.aschl.pgs.data;

public class ChoosePlayerMessage extends Message {

  private String playerChosen;

  public String getPlayerChosen() {
    return playerChosen;
  }

  public void setPlayerChosen(String playerChosen) {
    this.playerChosen = playerChosen;
  }
}
