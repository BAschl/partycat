package at.aschl.pgs.data.secrethitler;

import at.aschl.pgs.data.Room;
import at.aschl.pgs.data.secrethitler.ServerPlayerStatus.SecretHitlerRole;
import at.aschl.pgs.data.secrethitler.ServerPlayerStatus.Vote;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerGameStatus {

  public static final int START_LAW_COUNT_LIBERAL = 6;
  public static final int START_LAW_COUNT_FASCIST = 11;

  Room room;
  int liberalLawCount, fascistLawCount, failedVotes, liberalLawsLeft = START_LAW_COUNT_LIBERAL, fascistLawsLeft = START_LAW_COUNT_FASCIST;
  ServerPlayerStatus[] players;
  ServerPlayerStatus curPresident, nextPresident, curChancellor, nextChancellor;
  int presidentIdx;
  Lawtype[] currentLawChoice;

  public ServerGameStatus(Room room) {
    this.room = room;
    assignRoles(room);
    this.presidentIdx = (int) (Math.random() * players.length);
  }

  private void assignRoles(Room room) {
    List<String> users = room.getUsers();
    int size = users.size();

    //formula reverse engineered from rulebook table
    int amountFascists = (int) Math.round((size - 4) / 2.0d);
    int amountLiberals = size - amountFascists - 1;

    players = new ServerPlayerStatus[size];
    int roleCountLeft = size;
    for (int i = 0; i < players.length; i++) {
      SecretHitlerRole secretHitlerRole;

      int roleChooser = ((int) Math.random() * roleCountLeft);
      if (roleChooser == amountFascists + amountLiberals + 1) {
        secretHitlerRole = SecretHitlerRole.HITLER;
      } else if (roleChooser < amountLiberals) {
        secretHitlerRole = SecretHitlerRole.LIBERAL;
        amountLiberals--;
      } else {
        secretHitlerRole = SecretHitlerRole.FASCIST;
        amountFascists--;
      }
      roleCountLeft--;

      players[i] = new ServerPlayerStatus(users.get(i), secretHitlerRole);
    }
  }

  public void startNewRound() {
    while (players[++presidentIdx].killed) {
      ;
    }

    this.nextPresident = players[presidentIdx];
    for (ServerPlayerStatus player : players) {
      player.vote = Vote.NotVoted;
    }
  }


  public ServerPlayerStatus getNextPresident() {
    return nextPresident;
  }

  public ServerPlayerStatus[] getPlayers() {
    return players;
  }

  public ServerPlayerStatus getPlayerByName(String playerChosen) {
    Optional<ServerPlayerStatus> playerOptional = Arrays.stream(players)
        .filter(secretHitlerPlayer -> secretHitlerPlayer.getName().equals(playerChosen))
        .findFirst();

    if (playerOptional.isPresent()) {
      return playerOptional.get();
    }
    return null;
  }

  //todo see if chancellor is possible
  public void setNextChancellor(ServerPlayerStatus chancellor) {
    this.nextChancellor = chancellor;
  }

  public void vote(String userName, Vote vote) {
    getPlayerByName(userName).vote = vote;
    Vote allVotes = voteDone();
    if (allVotes == Vote.VotedYes) {
      this.curPresident = nextPresident;
      this.nextPresident = null;
      this.curChancellor = nextChancellor;
      this.nextChancellor = null;
      this.failedVotes = 0;
    } else {
      if (allVotes == Vote.VotedNo) {
        this.failedVotes++;
      }
    }
  }

  private Vote voteDone() {
    int yesVotes = 0, noVotes = 0;
    for (ServerPlayerStatus player : players) {
      if (player.vote == Vote.VotedYes) {
        yesVotes++;
      } else if (player.vote == Vote.VotedNo) {
        noVotes++;
      }
    }
    return yesVotes + noVotes == players.length ?
        yesVotes > noVotes ? Vote.VotedYes : Vote.VotedNo
        : Vote.NotVoted;
  }


  public Vote voteStatus() {
    List<Vote> votes = Arrays.stream(players)
        .map(secretHitlerPlayer -> secretHitlerPlayer.vote).collect(Collectors.toList());
    if (votes.stream().anyMatch(vote -> vote == Vote.NotVoted) == false) {
      int yesVotes = (int) votes.stream().filter(vote -> vote == Vote.VotedYes).count();
      if (yesVotes > (votes.size() - yesVotes)) {
        return Vote.VotedYes;
      } else {
        return Vote.VotedNo;
      }
    } else {
      return Vote.NotVoted;
    }
  }

  public ServerPlayerStatus getCurPresident() {
    return curPresident;
  }

  public Lawtype[] getOrGenerateCurrentLawChoice() {
    if (currentLawChoice == null) {
      currentLawChoice = new Lawtype[3];

      if (liberalLawsLeft + fascistLawsLeft < 3) {
        liberalLawsLeft = START_LAW_COUNT_LIBERAL - liberalLawCount;
        fascistLawsLeft = START_LAW_COUNT_FASCIST - fascistLawCount;
      }

      for (int i = 0; i < 3; i++) {
        int lawsLeft = liberalLawsLeft + fascistLawsLeft;
        if (Math.random() * lawsLeft < liberalLawsLeft) {
          currentLawChoice[i] = Lawtype.LIBERAL;
          liberalLawsLeft--;
        } else {
          currentLawChoice[i] = Lawtype.FASCIST;
          fascistLawsLeft--;
        }
      }
    }

    return currentLawChoice;
  }

  public Lawtype[] chooseLawToDismiss(int choice) {
    if (currentLawChoice.length == 2) {
      if(currentLawChoice[choice]==Lawtype.LIBERAL){
        liberalLawCount++;
      }else{
        fascistLawCount++;
      }
      return null;
    } else {
      Lawtype[] chancellorsChoice = new Lawtype[2];
      for (int i = 0, newI = 0; i < 3; i++, newI += choice == i ? 0 : 1) {
        if (choice != i) {
          chancellorsChoice[newI] = currentLawChoice[i];
        }
      }
      currentLawChoice = chancellorsChoice;
      return currentLawChoice;
    }
  }

  public ServerPlayerStatus getCurChancellor() {
    return curChancellor;
  }

  public enum Lawtype {
    FASCIST,
    LIBERAL
  }
}
