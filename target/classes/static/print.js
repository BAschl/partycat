
function uploadImg(){
  connectChatServer();
}

$(function(){
  $("#uploadImg").click(uploadImg);
    socket = new SocketManagement("printEndpoint", "", {});
})


var ws;

function connectChatServer() {
     ws = new WebSocket("ws://127.0.0.1/printEndpoint/socket");

     ws.onopen = function() {
         sendFile();
     };

     ws.onmessage = function(evt) {
         alert(evt.msg);
     };

     ws.onclose = function(code) {
      console.log("socket closed "+code.reason)
     };

     ws.onerror = function(e) {
       alert(e.msg);
     }
 }

 function sendFile() {
   var file = document.getElementById('filename').files[0];
   var reader = new FileReader();
   reader.onload = function(e) {
       ws.send(JSON.stringify({"filename":file.name, "imgdata":e.target.result}));
       //{ "filename":"img.jpg", "image": rawData});
       alert("the File has been transferred.")
   }
   reader.readAsDataURL(file);

   var reader2 = new FileReader();

      reader2.onload = function(e) {
          ws.send(e.target.result);
          //{ "filename":"img.jpg", "image": rawData});
          alert("the File has been transferred.")
      }
      reader2.readAsArrayBuffer(file);
 }

