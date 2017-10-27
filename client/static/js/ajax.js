var xhr = new XMLHttpRequest();


var get = function(url, okCallback, failCallback) {
	xhr.open('GET', url);
	xhr.onload = function() {
		if (xhr.status === 200) {
        	okCallback(JSON.parse(xhr.responseText));
    	} else {
    		failCallback();
    	}
	};
	xhr.send();
};

var post = function(url, body, okCallback, failCallback) {
	xhr.open('POST', url);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(body));
	xhr.onload = function() {
		if (xhr.status === 200) {
			okCallback(JSON.parse(xhr.responseText));
		} else {
			failCallback();
		}
	};
};

