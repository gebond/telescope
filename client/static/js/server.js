var socket = new WebSocket("ws://127.0.0.1:12345");

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

socket.onmessage = function (event) {

    alert("Getting data " + event.data);
};

socket.onerror = function (error) {
    alert("Error " + error.message);
};

window.onbeforeunload =
    function confirmExit() {
        socket.close();
        return false;
    };

// Send text to all users through the server
function sendMessage() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    var msg = {
        body: "message body",
        chat_id: 123,
        sender_id: 100,
        scope_id: 144
    };
    // Send the msg object as a JSON-formatted string.
    socket.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    //document.getElementById("text").value = "";
}

setTimeout(function () {
    sendMessage();
}, 1000);

function getMessages() {

}