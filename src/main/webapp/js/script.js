// Main JavaScript file for FineFish website

document.addEventListener("DOMContentLoaded", function() {
    // Mobile menu toggle
    const mobileMenuBtn = document.querySelector(".mobile-menu-btn");
    const navMenu = document.querySelector(".nav-menu");

    if (mobileMenuBtn && navMenu) {
        mobileMenuBtn.addEventListener("click", function() {
            navMenu.classList.toggle("show");
        });
    }

    // User dropdown toggle
    const userDropdownToggle = document.querySelector(".dropdown-toggle");
    const userDropdownMenu = document.querySelector(".dropdown-menu");

    if (userDropdownToggle && userDropdownMenu) {
        userDropdownToggle.addEventListener("click", function(e) {
            e.preventDefault();
            userDropdownMenu.classList.toggle("show");
        });

        // Close dropdown when clicking outside
        document.addEventListener("click", function(event) {
            if (!userDropdownToggle.contains(event.target) && !userDropdownMenu.contains(event.target)) {
                userDropdownMenu.classList.remove("show");
            }
        });
    }
});