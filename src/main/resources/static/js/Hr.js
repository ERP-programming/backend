// document.querySelectorAll('.tab-button').forEach(button => {
//     button.addEventListener('click', () => {
//         const tabButtons = document.querySelectorAll('.tab-button');
//         const tabContents = document.querySelectorAll('.tab-content');
//
//         tabButtons.forEach(btn => btn.classList.remove('active'));
//         tabContents.forEach(content => content.classList.remove('active'));
//
//         button.classList.add('active');
//         const tabName = button.getAttribute('data-tab');
//         document.querySelector(`.tab-content[data-tab="${tabName}"]`).classList.add('active');
//     });
// });

document.querySelectorAll('.tab-button').forEach(button => {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        const tabName = button.getAttribute('data-tab');

        if (tabName === 'hr') {
            fetch('/employees/hr-check')
                .then(response => response.text())
                .then(data => {
                    if (data === 'access-granted') {
                        activateTab(button, tabName);
                    } else {
                        alert('인사부서만 볼 수 있는 페이지입니다.');
                    }
                });
        } else {
            activateTab(button, tabName);
        }
    });
});

function activateTab(clickedButton, tabName) {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(btn => btn.classList.remove('active'));
    tabContents.forEach(content => content.classList.remove('active'));

    clickedButton.classList.add('active');
    document.querySelector(`.tab-content[data-tab="${tabName}"]`).classList.add('active');
}