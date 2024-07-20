// src/main/resources/static/js/modal.js

// Function to open a modal
function openModal(modalId, url) {
    if (url) {
        $('#' + modalId + ' .modal-content').load(url, function () {
            $('#' + modalId).modal('show');
        });
    } else {
        $('#' + modalId).modal('show');
    }
}

// Function to close a modal
function closeModal(modalId) {
    $('#' + modalId).modal('hide');
}

// Close the modal when the user clicks outside of it
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        closeModal(event.target.id);
    }
}
