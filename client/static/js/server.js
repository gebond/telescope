var socket = new WebSocket("ws://127.0.0.1:12345");

// socket utils callbacks
socket.onopen = function () {
    alert("Connection set up");
};
socket.onclose = function (event) {
    if (event.wasClean) {
        alert("Connection closed clearly");
    } else {
        alert("Conection interupted"); // например, "убит" процесс сервера
    }
    alert('Code: ' + event.code + ' cause: ' + event.reason);
};
socket.onerror = function (error) {
    alert("Error " + error.message);
};
window.onbeforeunload = function confirmExit() {
    socket.close();
    return false;
};

// message
socket.onmessage = function (event) {
    alert("Getting data " + event.data);
};

function sendMessage() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        func_type: "send_message",
        payload: {
            body: "message body",
            chat_id: 1,
            sender_id: "gbondarenko",
            scope_id: 2
        }
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}
setTimeout(function () {
    sendMessage();
}, 1000);