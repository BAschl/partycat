package at.aschl.pgs.data.secrethitler;

public class ServerPlayerStatus {

  String name;
  boolean killed;
  SecretHitlerRole secretHitlerRole;

  Vote vote = Vote.NotVoted;

  public ServerPlayerStatus(String name,
      SecretHitlerRole secretHitlerRole) {
    this.name = name;
    this.secretHitlerRole = secretHitlerRole;
  }

  public String getName() {
    return name;
  }

  public boolean isKilled() {
    return killed;
  }

  public SecretHitlerRole getSecretHitlerRole() {
    return secretHitlerRole;
  }

  public enum SecretHitlerRole {
    HITLER,
    FASCIST,
    LIBERAL
  }

  public enum Vote {
    NotVoted,
    VotedYes,
    VotedNo
  }

}
