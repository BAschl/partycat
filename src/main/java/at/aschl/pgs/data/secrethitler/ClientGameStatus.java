package at.aschl.pgs.data.secrethitler;

public class ClientGameStatus {

  String roomCode;
  int liberalLawsEnacted, fascsistLawsEnacted, failedVotes;
  ClientPlayerStatus[] players;

  public ClientGameStatus(ServerGameStatus game) {
    this.roomCode = game.room.getRoomCode();
    this.liberalLawsEnacted = game.liberalLawCount;
    this.fascsistLawsEnacted = game.fascistLawCount;
    this.failedVotes = game.failedVotes;
    this.players = new ClientPlayerStatus[game.players.length];
    for (int i = 0; i < game.players.length; i++) {
      ServerPlayerStatus player = game.players[i];
      this.players[i] = new ClientPlayerStatus(player,
          player.equals(game.curPresident) || player.equals(game.curChancellor));
    }
  }
}
