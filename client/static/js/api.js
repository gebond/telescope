API =
    {
        // login {user_id}
        login: function (login) {
            localStorage.setItem('userId', login.user_id);
            window.location.reload('/../');
        },

        loadContacts: function (chats) {

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