console.log("Connected to JS");

document.addEventListener("DOMContentLoaded", () => {
	console.log("Script started");
	setupEventListeners();

	function setupEventListeners() {
		document.addEventListener("mousemove", (event) => updateHeadAndEyes(event));

		document
			.querySelector("#dateForm")
			.addEventListener("submit", () => handleFormSubmit(event));
	}

	var userName = "";
	var userDate = "";

	function handleFormSubmit(event) {
		event.preventDefault();
		console.log("Clicked Submit");

        const target = event.target;
        const formData = new FormData(target);

        userName = formData.get('nameInput');
        userDate = formData.get('dateInput');

        console.log(userDate + " " + userName);

        moveFormAndCurtains();
	}

    function moveFormAndCurtains(){

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
		const mouseX = event.clientX;
		const mouseY = event.clientY;
		eyeBallRect = eyeDomElement.getBoundingClientRect();
		const eyeX = eyeBallRect.left + eyeBallRect.width / 2;
		const eyeY = eyeBallRect.top + eyeBallRect.height / 2;

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
});
