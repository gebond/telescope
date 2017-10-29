API =
    {
        // login {user_id}
        login: function (login) {
            localStorage.setItem('userId', login.payload.user_id);
            window.location.reload('/../');
        },

        loadContacts: function (chats) {
            chats = chats['chats'];
            var panel = document.getElementsByClassName('left')[0];
            panel.innerHTML = '';
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

            }
            var contactChats = document.getElementsByClassName('contact');
            for (var i = 0; i < contactChats.length; i++) {
                (function (chat, i) {
                    document.getElementById('contact_' + chat.id).addEventListener('click', function () {
                        getMessages({chat_id: chat.id, user_id: localStorage.getItem("userId")});
                        for (var j = 0; j < chats.length; j++) {
                            if (j !== i) {
                                document.getElementById('contact_' + chats[j].id).style['background'] = '#ffffff';
                                document.getElementById('contact_' + chats[j].id).style['color'] = '#111111';
                                document.getElementById('contact_' + chats[j].id).getElementsByClassName('last-message')[0].style['color'] = '#666666';
                            } else {
                                localStorage.setItem('chatId', chats[j].id);
                                document.getElementById('contact_' + chats[j].id).style['background'] = '#497799';
                                document.getElementById('contact_' + chats[j].id).style['color'] = '#ffffff';
                                document.getElementById('contact_' + chats[j].id).getElementsByClassName('last-message')[0].style['color'] = '#ffffff ';
                            }
                        }
                    })
                })(chats[i], i);
            }
            if (contactChats.length === 0) {
                contactChats.innerHTML = '<b>No chats yet</b>';
            }
            tuneAvatars();
        },

        loadMessages: function (messages) {
            messages = messages['messages'];
            var messageBox = document.getElementsByClassName('messages')[0];
            console.log(messages);
            messageBox.innerHTML = '';
            for (var i = 0; i < messages.length; i++) {
                messageBox.innerHTML += "<div class=\"message " + (messages[i].scope_id ? 'scope' : '') + " \" id='" +
                    (messages[i].scope_id || '') + "'>" +
                    "                        <span class=\"avatar\">" +
                    "                        " +
                    "                        </span>" +
                    "                        <div class=\"con-data\">" +
                    "                            <div class=\"user-name\">" +
                    (messages[i].sender_id) +
                    "                            </div>" +
                    "                            <div class=\"message-text\">" +
                    messages[i].body +
                    "                           </div>" +
                    "                        </div>" +
                    "<span style='color:grey;font-size:12px;margin: 6px;float:right'>" + new Date(messages[i].time).getHours() + ":" +
                     ((new Date(messages[i].time).getMinutes() < 10) ? "0" + new Date(messages[i].time).getMinutes() : new Date(messages[i].time).getMinutes()) +
                    "                     </span></div>"
            }
            if (messages.length === 0) {
                messageBox.innerHTML = '<b>No messages yet</b>';
            }
            tuneAvatars();
        },

        sendMessage: function () {
        },

        exit: function () {

        },

        // when we created chat
        createChat: function () {

        },

        // when someone add us to chat
        invitedToChat: function () {
            // reload chat
        },

        // when someone add us to chat
        loadScopes: function (scopes) {
            // reload chat
            scopes = scopes['scopes'];
            console.log(scopes);
        }
    }