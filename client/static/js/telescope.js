
var colorHashs = ['#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5', '#2196F3', '#03A9F4', '#00BCD4', '#009688', '#4CAF50', '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107', '#FF9800', '#FF5722', '#795548', '#9E9E9E', '#607D8B'];

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
			chats[userName].color = colorHashs[Math.floor((Math.random() * colorHashs.length) + 0)];
			//chats[userName].id = contactElms[i].id;
			//console.log(contactElms[i].className);
			avatarElms[i].style['background'] = chats[userName].color;
		} else {
			avatarElms[i].style['background'] = chats[userName].color;
		}
		avatarElms[i].innerHTML = '<span style="vertical-align:middle;display: inline-block;color:white;font-weight:bold;">' 
			+ userName[0].toUpperCase() + '</span>';
		console.log(userElms[i].innerText);
	}
}