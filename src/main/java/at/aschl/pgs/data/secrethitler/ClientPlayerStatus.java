package at.aschl.pgs.data.secrethitler;

import at.aschl.pgs.data.secrethitler.ServerPlayerStatus.Vote;

public class ClientPlayerStatus {
String username;
boolean killed;
boolean elligbleForElection;
Vote currentVote;

  public ClientPlayerStatus(ServerPlayerStatus player, boolean elligbleForElection) {
    this.username=player.name;
    this.killed=player.killed;
    this.currentVote=player.vote;
    this.elligbleForElection=elligbleForElection;
  }
}
