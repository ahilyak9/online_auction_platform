function login() {
    const userType = document.getElementById("userType").value;
    document.querySelector(".login-section").classList.add("hidden");
    
    if (userType === "seller") {
        document.querySelector(".seller-dashboard").classList.remove("hidden");
    } else if (userType === "buyer") {
        document.querySelector(".buyer-dashboard").classList.remove("hidden");
    } else if (userType === "admin") {
        document.querySelector(".admin-dashboard").classList.remove("hidden");
    }
}

function logout() {
    document.querySelector(".login-section").classList.remove("hidden");
    document.querySelectorAll(".dashboard").forEach(dashboard => dashboard.classList.add("hidden"));
}

function toggleAddProduct() {
    document.querySelector(".add-product").classList.toggle("hidden");
}

function toggleTrackPackage() {
    document.querySelector(".track-package").classList.toggle("hidden");
}

function toggleTrackPackageBuyer() {
    document.querySelector(".track-package-buyer").classList.toggle("hidden");
}

function addProduct() {
    alert("Product added!");
}

function trackPackage() {
    document.getElementById("trackingResult").innerText = "Tracking details for the entered Product ID.";
}

function viewProducts() {
    document.getElementById("productList").classList.remove("hidden");
}

function buyProduct() {
    alert("Product purchased!");
}

function trackPackageBuyer() {
    document.getElementById("trackingResultBuyer").innerText = "Tracking details for the entered Product ID.";
}