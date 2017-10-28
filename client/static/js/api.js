API =
    {
        // login {user_id}
        login: function (login) {
            localStorage.setItem('userId', login.user_id);
            window.location.reload('/../');
        },

        loadContacts: function (chats) {
            chats = chats['chats'];
            var panel = document.getElementsByClassName('left');
            for (var i = 0; i < chats.length; i++) {

            // <div class="contact">
            //         <span class="avatar">
            //
            //         </span>
            //         <div class="con-data">
            //         <div class="user-name">
            //         Petya
            //         </div>
            //         <div class="last-message">last message</div>
            //     </div>

                panel[0].innerHTML += '<div class="contact" id="' +
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
            tuneAvatars();
        },

        loadMessages: function (messages) {

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