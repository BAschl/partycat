class SocketManagement {

socket = {};
connected = false;
stompClient = {};
subPrefix = "topic";
endpointPrefix = "app";
subscriptions = [];

//set userId to Room+Username
userId = "";

constructor(address, userId, callBackFunctions){
  this.userId=userId;
  this.socket = new WebSocket("ws://127.0.0.1/"+address+"/websocket");
  this.stompClient = Stomp.over(this.socket);
  var This = this;
  this.stompClient.connect({}, function(frame){
    This.connected = true;
    This.multiSubscribe(callBackFunctions);
  });
}


multiSubscribe(callBackFunctions){
  var keys = Object.keys(callBackFunctions);
  var This = this;
  var newSubs = [];

  keys.forEach( function(key){
    newSubs[newSubs.length]=This.subscribe(key, callBackFunctions[key]);
  });

  return newSubs;
}

subscribe(address, callBackFunction){
  var user = "";
  if(this.userId!=null && this.userId.length > 0){
    user="/"+this.userId;
  }

  console.log(Object.getOwnPropertyNames(callBackFunction));
  var sub = {};
  sub.name = address;
  sub.func = callBackFunction;
  sub.sub = this.stompClient.subscribe("/"+this.subPrefix+user+"/"+address, callBackFunction);

  this.subscriptions[this.subscriptions.length]=sub;
  return sub;
}


unsubscribe(subscriptions){
  subscriptions.forEach(function(sub){
    sub.unsubscribe();
  });
}

send(endpointAddress, object){
  this.stompClient.send("/"+this.endpointPrefix+"/"+endpointAddress, {}, JSON.stringify(object));
}

emptySend(endpointAddress){
  this.send(endpointAddress, null);
}

setUserIdAndResub(userId){
  var callBackFunctions = {};
  this.userId = userId;
  this.subscriptions.forEach(function(sub){
    sub.sub.unsubscribe();
    callBackFunctions[sub.name] = sub.func;
  })

  this.subscriptions=[];
  this.multiSubscribe(callBackFunctions);
}

}