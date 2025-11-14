console.log("Connected to JS");

document.addEventListener("DOMContentLoaded", () => {
	console.log("Script started");
	function generateSessionId() {
		return (
			Math.random().toString(36).substring(2, 15) + Date.now().toString(36)
		);
	}

	let sessionId = generateSessionId();
	console.log(sessionId);

	let firstMessage = true;

	var userName = "";
	var userDate = "";

	EyesUp = false;

	const url = "http://localhost:8080/api/astrochat";

	setupEventListeners();

	function setupEventListeners() {
		document.addEventListener("mousemove", (event) => updateHeadAndEyes(event));

		document
			.querySelector("#dateForm")
			.addEventListener("submit", () => handleFormSubmit(event));

		document
			.querySelector("#chatSend")
			.addEventListener("submit", () => handleChatSend(event));
	}

	function handleChatSend(event) {
		event.preventDefault();
		const target = event.target;
		const formData = new FormData(target);

		startThinking();
		clearCrystalBall();

		sendMessage(formData.get("questionBox"), userName, userDate);
	}

	function clearCrystalBall(){
		const questionBox = document.querySelector("#questionBox");
		questionBox.value = "";
	}


	function startThinking(){
		EyesUp = true;

		const sendButton = document.querySelector("#sendButton");
		sendButton.style.transform = "translate(0, 300%)";
		sendButton.innerHTML = "Thinking";
	}

	function stopThinking(){
		EyesUp = false;
		const sendButton = document.querySelector("#sendButton");
		sendButton.style.transform = "translate(0, 0)";
		sendButton.innerHTML = "Send";
	}

	async function sendMessage(question, userName, userDate) {
		addMessageToDom(question, "user");

		let answer = "My child, the stars fail me. I have lost the vision beyond, and I am unable to help you at this time. May the currents of time guide you, when I cannot. Come back a little later, when the heavens once again open to me.";
		if (firstMessage) {
			try{
				answer = await sendFirstMessage(question, userName, userDate);
				console.log(answer.choices[0].message.content);
			}
			catch{}
		} else {
			try{
				answer = await sendFollowMessage(question);
			}
			catch{}
		}
		stopThinking();
		try{
		displayAnswer(answer.choices[0].message.content);
		}
		catch{
			displayAnswer("I had trouble consulting the stars. They seem to tell me: Error code: 500 - internal server error. I do not know what that means... weird stars huh?")
		}
	}

	function addMessageToDom(message, role) {
		const messageDom = document.createElement("div");
		messageDom.classList.add("message");

		const roleChild = document.createElement("div");
		roleChild.classList.add("roleLabel");

		if (role === "ai") {
			messageDom.classList.add("aiMessage");
			roleChild.innerHTML = "Chataztro";
		} else {
			messageDom.classList.add("userMessage");
			roleChild.innerHTML = userName;
		}

		messageDom.appendChild(roleChild);

		const messageChild = document.createElement("div");
		messageChild.classList.add("messageText");
		messageChild.innerHTML = message;

		messageDom.appendChild(messageChild);

		const messageBox = document.querySelector("#answerTextBox");
		messageBox.appendChild(messageDom);
		messageBox.scrollTop = messageBox.scrollHeight;
	}

	function displayAnswer(answer) {
		addMessageToDom(answer, "ai");

		if (firstMessage) {
			displayParchment();
			firstMessage = false;
		}
	}

	async function sendFirstMessage(question, userName, userDate) {
		console.log("Sending the first message");

		const message = {
			name: userName,
			date: userDate,
			userMessage: question,
			sessionId: sessionId,
		};

		const options = createFetchOptions("POST", message);
		const res = await fetch(url + "/send", options);
		return responseHandler(res);
	}

	async function sendFollowMessage(question) {
		console.log("Sending a new message");

		const message = {
			userMessage: question,
			sessionId: sessionId,
		};

		const options = createFetchOptions("POST", message);
		const res = await fetch(url, options);
		return responseHandler(res);
	}

	async function responseHandler(res) {
		// 204 = no content
		if (res.status === 204) {
			return null;
		}

		if (!res.ok) {
			throw new Error("HTTP error. status: " + res.status);
		}

		const contentType = res.headers.get("content-type") || "";
		if (!contentType.includes("application/json")) {
			return null;
		}
		return res.json();
	}

	function displayParchment() {
		const parchment = document.querySelector("#answerParchment");
		parchment.style.transform = "translate(160%, 10%)";
	}

	function handleFormSubmit(event) {
		event.preventDefault();
		console.log("Clicked Submit");

		const target = event.target;
		const formData = new FormData(target);

		userName = formData.get("nameInput");
		userDate = formData.get("dateInput");

		console.log(userDate + " " + userName);

		moveFormAndCurtains();
	}

	function moveFormAndCurtains() {
		const form = document.querySelector("#dateForm");
		form.style.transform = "translate(-50%, 300%)";

		const leftCurtain = document.querySelector("#leftCurtain");
		const rightCurtain = document.querySelector("#rightCurtain");

		leftCurtain.style.transform = "translate(-100%, -30%) rotate(20deg)";
		rightCurtain.style.transform = "translate(100%, -30%) rotate(-20deg)";
	}

	function updateHeadAndEyes(event) {
		const headDomElement = document.querySelector(".headBox");
		updateHead(event, headDomElement);

		const eye1 = document.querySelector(".firstEye");
		updateEye(event, eye1);

		const eye2 = document.querySelector(".secondEye");
		updateEye(event, eye2);
	}

	// Define these outside the function so they persist between calls
	let currentX = 0;
	let currentY = 0;

	function updateHead(event, headElement) {
		const mouseX = event.clientX;
		const mouseY = event.clientY;

		const headRect = headElement.getBoundingClientRect();
		const headX = headRect.left + headRect.width / 2;
		const headY = headRect.top + headRect.height / 2;

		const vectorX = mouseX - headX;
		const vectorY = mouseY - headY;

		const distance = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
		const maxDistance = headRect.width / 15;

		let targetX = vectorX;
		let targetY = vectorY;

		if (distance > maxDistance) {
			const scale = maxDistance / distance;
			targetX *= scale;
			targetY *= scale;
		}

		// Smoothing factor — lower = smoother/slower
		const smooth = 0.05;

		// Lerp toward the target position
		currentX += (targetX - currentX) * smooth;
		currentY += (targetY - currentY) * smooth;

		const headImg = headElement.firstElementChild;
		headImg.style.transform = `translate(0, 5%) translate(${currentX}px, ${currentY}px)`;
	}

	function updateEye(event, eyeDomElement) {
		var mouseX = event.clientX;
		var mouseY = event.clientY;

		eyeBallRect = eyeDomElement.getBoundingClientRect();
		const eyeX = eyeBallRect.left + eyeBallRect.width / 2;
		const eyeY = eyeBallRect.top + eyeBallRect.height / 2;

		if(EyesUp){
			mouseX = eyeX;
			mouseY = eyeY + -200;
		}

		const vectorX = mouseX - eyeX;
		const vectorY = mouseY - eyeY;

		const distance = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
		const maxDistance = eyeBallRect.width / 3.5;

		let offsetX = vectorX;
		let offsetY = vectorY;
		if (distance > maxDistance) {
			const scale = maxDistance / distance;
			offsetX *= scale;
			offsetY *= scale;
		}

		const angle = Math.atan2(mouseY - eyeY, mouseX - eyeX);

		const scaling = mapRangeClamped(distance);

		const iris = eyeDomElement.firstElementChild;
		iris.style.transform = `translate(-50%, -50%) translate(${offsetX}px, ${offsetY}px) rotate(${
			(angle * 180) / 3.14
		}deg) scaleX(${scaling})`;
	}

	function mapRangeClamped(
		value,
		inMin = 7,
		inMax = 14,
		outMin = 1,
		outMax = 0.9
	) {
		const t = Math.max(0, Math.min(1, (value - inMin) / (inMax - inMin)));
		return t * (outMax - outMin) + outMin;
	}

	// Method to build fetch type
	function createFetchOptions(httpMethod, body, headers = {}) {

		// buildes options obj
		const options = {
			method: httpMethod,
			headers: {
				Accept: "application/json",
				...headers,
			},
		};

		// Not all requests require a body, hence the if statement
		if (body) {
			// Converts the object to a JSON string
			options.body = JSON.stringify(body);
			options.headers["Content-Type"] = "application/json";
		}
		return options;
	}
});
