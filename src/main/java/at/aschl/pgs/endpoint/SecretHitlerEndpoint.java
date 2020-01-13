package at.aschl.pgs.endpoint;

import at.aschl.pgs.data.ChoosePlayerMessage;
import at.aschl.pgs.data.IntMessage;
import at.aschl.pgs.data.Room;
import at.aschl.pgs.data.secrethitler.ClientGameStatus;
import at.aschl.pgs.data.secrethitler.ServerGameStatus;
import at.aschl.pgs.data.secrethitler.ServerGameStatus.Lawtype;
import at.aschl.pgs.data.secrethitler.ServerPlayerStatus;
import at.aschl.pgs.data.secrethitler.ServerPlayerStatus.Vote;
import java.util.HashMap;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class SecretHitlerEndpoint {

  private final SimpMessageSendingOperations messagingTemplate;

  private Map<String, ServerGameStatus> gameStati = new HashMap<>();

  public SecretHitlerEndpoint(
      SimpMessageSendingOperations messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void startGame(Room room) {
    ServerGameStatus gameStatus = new ServerGameStatus(room);
    gameStati.put(room.getRoomCode(), gameStatus);

    for (ServerPlayerStatus player : gameStatus.getPlayers()) {
      messagingTemplate
          .convertAndSend("/topic/" + room.getRoomCode() + "/" + player.getName() + "init", player);
    }

    gameStatus.startNewRound();
    startNewRound(room.getRoomCode());

  }

  private void startNewRound(String roomCode) {
    ServerGameStatus gameStatus = gameStati.get(roomCode);
    messagingTemplate.convertAndSend("/topic/" + roomCode + "/newRound",
        gameStatus.getNextPresident().getName());
    messagingTemplate
        .convertAndSend("/topic/" + roomCode + "/gameStatusUpdate",
            new ClientGameStatus(gameStatus));
  }

  @MessageMapping("/chooseChancellor")
  public void chooseChancellor(ChoosePlayerMessage choosePlayerMessage) {
    if (gameStati.containsKey(choosePlayerMessage.getRoomCode())) {
      ServerGameStatus gameStatus = gameStati.get(choosePlayerMessage.getRoomCode());
      if (gameStatus.getNextPresident().getName().equals(choosePlayerMessage.getUserName())) {
        ServerPlayerStatus chancellor = gameStatus
            .getPlayerByName(choosePlayerMessage.getPlayerChosen());
        gameStatus.setNextChancellor(chancellor);

        messagingTemplate
            .convertAndSend("/topic/" + choosePlayerMessage.getRoomCode() + "/chancellorChosen",
                chancellor.getName());
      }
    }
  }

  @MessageMapping("/vote")
  public void vote(IntMessage choice) {
    String roomCode = choice.getRoomCode();
    if (gameStati.containsKey(roomCode)) {
      ServerGameStatus gameStatus = gameStati.get(roomCode);
      gameStatus.vote(choice.getUserName(), choice.getChoice() == 0 ? Vote.VotedNo : Vote.VotedYes);
      messagingTemplate
          .convertAndSend("/topic/" + roomCode + "/gameStatusUpdate",
              new ClientGameStatus(gameStatus));

      Vote groupVoteStatus = gameStatus.voteStatus();
      if (groupVoteStatus != Vote.NotVoted) {
        messagingTemplate
            .convertAndSend("/topic/" + roomCode + "/gameStatusUpdate",
                new ClientGameStatus(gameStatus));
        if (groupVoteStatus == Vote.VotedYes) {
          messagingTemplate
              .convertAndSend("/topic/" + roomCode + "/" + gameStatus.getCurPresident().getName()
                  + "/presidentsChoice", gameStatus.getOrGenerateCurrentLawChoice());
        }
      }
    }
  }

  @MessageMapping("/lawchoice")
  public void lawChoice(IntMessage choice) {
    ServerGameStatus gameStatus = gameStati.get(choice.getRoomCode());
    if(gameStatus.getCurPresident().getName().equals(choice.getUserName())){

      Lawtype[] remainingLaws = gameStatus.chooseLawToDismiss(choice.getChoice());
      messagingTemplate
          .convertAndSend("/topic/" + choice.getRoomCode()+ "/" + gameStatus.getCurChancellor().getName()
              + "/chancellorsChoice", remainingLaws);
    }else {
      //todo unauthorized
    }
  }

  @MessageMapping("/enactLaw")
  public void enactLaw(IntMessage choice){
    ServerGameStatus gameStatus = gameStati.get(choice.getRoomCode());
    if(gameStatus.getCurChancellor().getName().equals(choice.getUserName())){
      gameStatus.chooseLawToDismiss(choice.getChoice());
      startNewRound(choice.getRoomCode());
    }else{
      //todo unauthorized
    }
  }


}
