var socket = new WebSocket("ws://127.0.0.1:12345");

// socket utils callbacks
socket.onopen = function () {
    console.log("Connection set up");
};
socket.onclose = function (event) {
    if (event.wasClean) {
        console.log("Connection closed clearly");
    } else {
        console.log("Conection interupted"); // например, "убит" процесс сервера
    }
    console.log('Code: ' + event.code + ' cause: ' + event.reason);
    API.exit();
};
socket.onerror = function (error) {
    API.exit();
    console.log("Error " + error.message);
};
window.onbeforeunload = function confirmExit() {
    socket.close();
    API.exit();
    return false;
};

// message
socket.onmessage = function (event) {
    console.log("Getting data " + event.data);
    data_json = JSON.parse(event.data);
    if (data_json.method === "login") {
        API.login(data_json.payload)
    }
    else if (data_json.method === "get_messages") {
        API.loadMessages(data_json.payload)
    }
    else if (data_json.method === "get_chats") {
        API.loadContacts(data_json.payload)
    }
    else if (data_json.method === "send_message") {
        API.sendMessage(data_json.payload)
    }
    else if (data_json.method === "create_chat") {
        API.createChat(data_json.payload)
    }
    else if (data_json.method === "invite_to_chat") {
        API.inviteToChat(data_json.payload)
    }
    else {
        console.log("wrong message!")
    }
};

// login = [{user_name}]
function login(login) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        func_type: "login",
        payload: login
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));
    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}

// sendmessage = {text, chat_id, sender_id, scope_id}
function sendMessage(sendmessage) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        func_type: "send_message",
        payload: sendmessage
        // {body: "message body",
        // chat_id: 1,
        // sender_id: "gbondarenko",
        // scope_id: 2}
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));
    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}

// setTimeout(function () {
//     sendMessage();
// }, 1000);

// messages =  [{chat_id, user_id}]
function getMessages(getmessages) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        func_type: "get_messages",
        payload: getmessages
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));
    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}

// sendmessage = [{user_id}]
function getChats(getchats) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        func_type: "get_chats",
        payload: getchats
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));
    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}
