API =
    {
        // login {user_id}
        login: function (login) {
            localStorage.setItem('userId', login.user_id);
            window.location.reload('/../');
        },

        loadContacts: function (chats) {
            chats = chats['chats'];
            var panel = document.getElementsByClassName('left')[0];
            for (var i = 0; i < chats.length; i++) {
                panel.innerHTML += '<div class="contact" id="contact_' +
                    chats[i].id + '">' +
                    '                    <div class="avatar">' +
                    '                        ' +
                    '                    </div>' +
                    '                        <div class="con-data">' +
                    '                            <div class="user-name">' +
                    chats[i].name +
                    '                            </div>' +
                    '                            <div class="last-message">' +
                    (chats[i].lastMessageText || '') + '</div>' +
                    '                        </div>' +
                    '                </div>';
                var chat = chats[i];
                (function (chat) {
                    document.getElementById('contact_' + chat.id).addEventListener('click', function () {
                        console.log(chat.id);// getMessages({chat_id: chat.id, user_id: localStorage.getItem("userId")});
                    })
                })(chat);


            }
            tuneAvatars();
        },

        loadMessages: function (messages) {

            var messageBox = document.getElementsByClassName('messages')[0];

            for (var i = 0; i < messages.length; i++) {
                messageBox.innerHTML += "<div class=\"message scope\" id='" +
                    (messages[i].scope_id || '') + "'>" +
                    "                        <span class=\"avatar\">" +
                    "                        " +
                    "                        </span>" +
                    "                        <div class=\"con-data\">" +
                    "                            <div class=\"user-name\">" +
                    messages[i].user_name +
                    "                            </div>" +
                    "                            <div class=\"message-text\">" +
                    messages[i].body +
                    "                            </div>" +
                    "                        </div>" +
                    "                    </div>"
            }
        },

        sendMessage: function () {
            // reload chat
        },

        exit: function () {

        },

        // when we created chat
        createChat: function () {

        },

        // when someone add us to chat
        invitedToChat: function () {
            // reload chat
        }
    }