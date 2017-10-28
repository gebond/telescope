
var colorHashs =      ['#F44336', '#9C27B0', '#3F51B5', '#2196F3', '#8BC34A', '#FF9800'];
var lightColorHashs = ['#EF9A9A', '#CE93D8', '#9FA8DA', '#90CAF9', '#C5E1A5', '#FFCC80'];

var chats = {};

var tuneAvatars = function(name) {
	var contactElms = document.getElementsByClassName('contact');
	var messageElms = document.getElementsByClassName('message');
	var avatarElms = document.getElementsByClassName('avatar');
	var userElms = document.getElementsByClassName('user-name');
	for (var i = 0; i < avatarElms.length; i++) {
		var userName = userElms[i].innerText;
		if (!chats[userName]) {
			chats[userName] = {};
			var num = Math.floor((Math.random() * colorHashs.length) + 0);
			chats[userName].color = colorHashs[num];
			avatarElms[i].style['background'] = chats[userName].color;
			chats[userName].id = contactElms[i] ? contactElms[i].getAttribute('id').split('_')[1] : '';
			
			//messageElms[i].style['background'] = 
		} else {
			avatarElms[i].style['background'] = chats[userName].color;
		}
		avatarElms[i].innerHTML = '<span style="vertical-align:middle;display: inline-block;color:white;font-weight:bold;">' 
			+ userName[0].toUpperCase() + '</span>';
	}
	for (var i = 0; i < messageElms.length; i++) {
		var chatId = messageElms[i].getAttribute('id');
		if (chatId) {
			var bColor;
			for (var j in chats) {
				if (chats[j].id === chatId) {
					bColor = chats[j].color;
					break;
				}
			}
			messageElms[i].style['background'] = lightColorHashs[colorHashs.indexOf(bColor)];
		}
	}
}