socket = null;

joiningRoom = true;

function joinRoom(roomCode){
  var username =$("#username").val();
  socket.setUserIdAndResub(roomCode);
  socket.send("joinRoom", {'roomCode':roomCode, 'userName' : username})
}

function preJoinRoomClicked(){
  $("#joinRoom")[0].style.display="block";
  $("#roomcode").show();
  $("#roomcodeLabel").show();
  $("#h1JoinRoom").text("join room");
  joiningRoom=true;
}

function createRoom(){
  $("#joinRoom")[0].style.display="block";
  $("#roomcode").hide();
  $("#roomcodeLabel").hide();
  $("#h1JoinRoom").text("create room");
  joiningRoom=false;
}

function joinOrCreate(){
  if(joiningRoom){
    joinRoom($("#roomCode").val());
  }else{
    socket.emptySend("createRoom");
  }
}

$(function(){
  $("#create").click(createRoom);
  $("#join").click(preJoinRoomClicked);
  $("#confirmJoin").click(joinOrCreate)
  socket = new SocketManagement("room", "", roomCallBackFunctions);
})

message = null;

roomCallBackFunctions = {
  "roomCreated" : function (roomCodeMessage) {
    socket.setUserIdAndResub(roomCodeMessage.body);
    joinRoom(roomCodeMessage.body);
  },

  "roomJoined": function (roomJoined){
    $("#mainTable").attr("hidden", "");
    $("#roomTable").removeAttr("hidden");
    var ul = $("#usersList");
    ul.empty();
    JSON.parse(roomJoined.body).users.forEach(function(user){
      var li = document.createElement("li");
      li.appendChild(document.createTextNode(user));
      ul.append(li);
    });

  }
};