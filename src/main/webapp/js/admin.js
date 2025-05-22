// Admin dashboard JavaScript

document.addEventListener("DOMContentLoaded", function () {
  // Delete confirmation modal
  const deleteButtons = document.querySelectorAll(".btn-action.delete");
  const deleteModal = document.getElementById("deleteModal");
  const closeModalButtons = document.querySelectorAll(".close, .close-modal");
  const deleteProductId = document.getElementById("deleteProductId");
  const deleteProductName = document.getElementById("deleteProductName");
  
  if (deleteButtons && deleteModal) {
    // Open modal when delete button is clicked
    deleteButtons.forEach(button => {
      button.addEventListener("click", function () {
        const productId = this.getAttribute("data-product-id");
        const productName = this.getAttribute("data-product-name");
        
        deleteProductId.value = productId;
        deleteProductName.textContent = productName;
        
        deleteModal.style.display = "block";
      });
    });
    
    // Close modal when close button is clicked
    if (closeModalButtons) {
      closeModalButtons.forEach(button => {
        button.addEventListener("click", function () {
          deleteModal.style.display = "none";
        });
      });
    }
    
    // Close modal when clicking outside
    window.addEventListener("click", function (event) {
      if (event.target === deleteModal) {
        deleteModal.style.display = "none";
      }
    });
  }
  
});



