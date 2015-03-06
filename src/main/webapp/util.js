function RequestObject() {
	var req = new XMLHttpRequest();
	var contentType = "";
	var headers = [];
	var responseType = "";

	return {
		getXhr : function(){
			return req;
		},
		
		addListener : function(event, func, func2) {
			if (typeof(func) == "function") {
				req.addEventListener(event, func);
			} else if (typeof(func) == "string") {
				if (func2) {
					req.addEventListener(event, function() {
						draw(func, "HTTP Status: " + req.status + "<br>" + req.response);
						func2();
					});
				} else {
					req.addEventListener(event, function() {
						draw(func, "HTTP Status: " + req.status + "<br>" + req.response);
					});
				}
			}
		},
		
		setContentType : function(value) {
			contentType = value;
		}, 
		
		setResponseType : function(value) {
			responseType = value;
		},
		
		addHeader : function(header, value) {
			headers.push({"header":header, "value":value});
		},
		
		send : function(method, path, sendData) {
			req.open(method, "api/" + path);
			
			if (contentType != "") {
				req.setRequestHeader("Content-Type", contentType);
			}
			if (responseType != "") {
				req.responseType = responseType;
			}
			
			if (headers.length > 0) {
				headers.forEach(function(elm){
					req.setRequestHeader(elm.header, elm.value);
				});
			}
			
			// Jerseyのフィルターテスト用
			// 外すと全てのリクエストが400になる
			if (document.forms.common.xpoweredby.checked) {
				req.setRequestHeader("X-Powered-By", "tsz662");
			}
			
			req.send(sendData);
		}
	};
}

function draw(nodeId, result, isAppend) {
	var target = document.getElementById(nodeId);
	var p = document.createElement("p");
	p.innerHTML = result;
	if (!isAppend) {
		target.innerHTML = "";
	}
	target.appendChild(p);
}

function drawBlob(nodeId, blob) {
	console.log("received -->");
	console.log(blob);
	var target = document.getElementById(nodeId);
	var img = document.createElement("img");
	img.addEventListener("loadend", function(e) {
		window.URL.revokeObjectURL(img.src);
	});
	img.src = window.URL.createObjectURL(blob);
	img.width = "600";

	if (target.firstElementChild) {
		target.removeChild(target.firstElementChild);
	}
	target.appendChild(img);
}