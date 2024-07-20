document.querySelectorAll('.tab-button').forEach(button => {
    button.addEventListener('click', () => {
        const tabButtons = document.querySelectorAll('.tab-button');
        const tabContents = document.querySelectorAll('.tab-content');

        tabButtons.forEach(btn => btn.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        button.classList.add('active');
        const tabName = button.getAttribute('data-tab');
        document.querySelector(`.tab-content[data-tab="${tabName}"]`).classList.add('active');
    });
});