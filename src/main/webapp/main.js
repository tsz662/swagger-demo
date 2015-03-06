window.addEventListener("load", getContacts);

var forms = document.forms;

/***********Contacts*****************/
/** Gets all contacts. */
function getContacts() {
	var req = new RequestObject();
	req.addListener("loadend", "contacts");
	req.send("get", "contacts", null);
}

/** Gets the specified contact. */
function getContact(id) {
	var req = new RequestObject();
	req.addListener("loadend", "getContactResult");
	req.send("get", "contacts/" + id, null);
}

/** Gets the specified attribute of the specified contact. */
function getContactInfo(id, attr, cookie) {
	var req = new RequestObject();
	req.addListener("loadend", "getContactInfoResult");
	req.addHeader("Cookie", "hoge=" + cookie);
	req.send("get", "contacts/" + id + "/attrib?field=" + attr, null);
}

/** Registers a contact. */
function createContact(name, address, addCustomHeaders) {
	var obj = {"name":name, "address":address};
	
	var req = new RequestObject();
	req.addListener("loadend", "createContactResult", getContacts);
	req.setContentType("application/json; charset=utf-8");
	if (addCustomHeaders) {
		req.addHeader("X-Auth", "foo");
		req.addHeader("X-CSRF", "bar");
	} else {
		req.addHeader("X-Auth", "liar");
		req.addHeader("X-CSRF", "liar");
	}
	req.send("post", "contacts", JSON.stringify(obj));
}

/** Updates the specified contact. */
function updateContact(id, name, address) {
	if (id !== "" || name !== "" || address !== "") {
		var obj = new Object();
		if (name !== "") {
			obj.name = name;
		}
		if (address !== "") {
			obj.address = address;
		}

		var req = new RequestObject();
		req.addListener("loadend", "updateContactResult", getContacts);
		req.setContentType("application/json; charset=utf-8");
		req.send("put", "contacts/" + id, JSON.stringify(obj));
	}
}

/** Deletes the specified contact. */
function deleteContact(id) {
	var req = new RequestObject();
	req.addListener("loadend", "deleteContactResult", getContacts);
	req.send("delete", "contacts/" + id, null);
}


/**********************************************************/
function getImage(filename, responseType) {
	var req = new RequestObject();
	var func = function() {
		if (this.status === 200 && this.response) {
			switch (responseType) {
			case "arraybuffer":
				// 受信データはArrayBuffer型だが、データの取り扱いが不便な為、ArrayBufferViewのサブクラスであるUint8Arrayとして扱う
				var view = new Uint8Array(this.response);
				// ArrayBufferView(のサブクラスであるUint8Array)とMimeTypeを渡すことでBlobオブジェクトを作成
				var contentType = this.getResponseHeader("Content-Type");
				console.log(contentType);
				var blob = new Blob([view], {"type": "image/jpeg"});
				drawBlob("getImageResult", blob);
				break;
			case "blob":
				drawBlob("getImageResult", this.response);
				break;
			}
		} else {
			draw("getImageResult", this.status);
		}
	};
	req.addListener("load", func.bind(req.getXhr()));
	req.setResponseType(responseType);
	req.send("get", "files/images/?filename=" + filename + "&responseType=" + responseType);
}

/****
 * StreamingOutputテスト
 */
function testStreamingOutput() {
	var xhr = new XMLHttpRequest();
	xhr.open("get", "api/files/streamingoutput", true);
	
	var nextLine = 0;

	xhr.addEventListener("progress", function() {
		 //readyState: headers received 2, body received 3, done 4
		if (xhr.readyState != 2 && xhr.readyState != 3 && xhr.readyState != 4) {
			return;
		}
		if (xhr.readyState == 3 && xhr.status != 200) {
			return;
		}

		var slice =xhr.response.slice(nextLine);
		console.log("receiving...");
		draw("testStreamingOutputResult", slice);
		nextLine = xhr.response.length;
	});

	xhr.addEventListener("load", function () {
		console.log("---done---");
	 });
	xhr.addEventListener("error", function () {
		console.log("---error---");
	});
	xhr.addEventListener("abort", function () {
		console.log("---aborted---");
	});
	
	if (document.forms.common.xpoweredby.checked) {
		xhr.setRequestHeader("X-Powered-By", "tsz662");
	}

	xhr.send();
}

/**
 * multipartのテスト 
 */
function uploadFiles(form) {
	// FormDataオブジェクト(multipart)
	var fd  = new FormData(form);

	var req = new RequestObject();
	var func = function() {
		draw("uploadFileResult", "HTTP Status: " + this.status + "<br>" + this.response);
	};
	req.addListener("loadend", func.bind(req.getXhr()));
	req.send("post", "files/multipart", fd);
}

/*****************************************************************/
function exmapperTest() {
	var req = new RequestObject();
	var func = function() {
		if (this.readyState == 4) {
			var headers = this.getAllResponseHeaders();
			draw("exmapperTestResult", "<strong>Status: </strong>" + this.status 
					+ "<br><strong>Headers: </strong>" + headers
					+ "<br><strong>Body: </strong>" + this.response);
		}
	};
	req.addListener("loadend", func.bind(req.getXhr()));
	req.send("post", "others/", null);
}

/**
 * formのテスト
 */
function echoThis(value) {
	var req = new RequestObject();
	req.addListener("loadend", "echoResult");
	req.setContentType("application/x-www-form-urlencoded");
	req.send("post", "others/form", "input=" + value);
}
