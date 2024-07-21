function openTab(evt, tabName) {
    var i, tabcontent, tabbuttons;

    // 모든 탭 내용 숨기기
    tabcontent = document.getElementsByClassName("tab-content");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // 모든 탭 버튼에서 'active' 클래스 제거
    tabbuttons = document.getElementsByClassName("tab-button");
    for (i = 0; i < tabbuttons.length; i++) {
        tabbuttons[i].className = tabbuttons[i].className.replace(" active", "");
    }

    // 선택한 탭 내용 보여주기
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";



}

// 페이지가 로드될 때 notice 탭을 열도록 초기화
window.onload = function() {
    // '공지사항' 탭 클릭 이벤트를 트리거
    var noticeButton = document.querySelector('.tab-button.active');
    if (noticeButton) {
        openTab({ currentTarget: noticeButton }, 'notice');
    }
};

// J
// S
// C

// 문서가 로드되면 실행될 함수
// Notice 모달 관련 기능
document.addEventListener('DOMContentLoaded', function() {
    // 필요한 NOTICE DOM 요소들 가져오기
    const noticeTable = document.querySelector('#notice table');  // 공지사항 목록 테이블
    const noticeDetailModal = document.getElementById('noticeDetailModal');  // 상세 모달
    const createNoticeModal = document.getElementById('createNoticeModal');  // 생성 모달
    const editNoticeModal = document.getElementById('editNoticeModal');  // 수정 모달
    // 필요한 TASK DOM 요소들 가져오기
    const taskTable = document.querySelector('#task table');  // 작업 목록 테이블
    const taskDetailModal = document.getElementById('taskDetailModal');  // 상세 모달
    const createTaskModal = document.getElementById('createTaskModal');  // 생성 모달
    const editTaskModal = document.getElementById('editTaskModal');  // 수정 모달
    // 각 NOTICE 모달의 닫기 버튼 가져오기
    const noticeDetailCloseSpan = noticeDetailModal.querySelector('.close');
    const createNoticeCloseSpan = createNoticeModal.querySelector('.close');
    const editNoticeCloseSpan = editNoticeModal.querySelector('.close');
    // 각 TASK 모달의 닫기 버튼 가져오기
    const taskDetailCloseSpan = taskDetailModal.querySelector('.close');
    const createTaskCloseSpan = createTaskModal.querySelector('.close');
    const editTaskCloseSpan = editTaskModal.querySelector('.close');

    // 모달 닫기 함수
    function closeModal(modal) {
        modal.style.display = "none";
    }

    // 모달과 닫기 버튼 매핑
    const modals = [
        { modal: noticeDetailModal, closeSpan: noticeDetailCloseSpan },
        { modal: createNoticeModal, closeSpan: createNoticeCloseSpan },
        { modal: editNoticeModal, closeSpan: editNoticeCloseSpan },
        { modal: taskDetailModal, closeSpan: taskDetailCloseSpan },
        { modal: createTaskModal, closeSpan: createTaskCloseSpan },
        { modal: editTaskModal, closeSpan: editTaskCloseSpan }
    ];

    // 모든 모달에 대해 닫기 버튼 이벤트 리스너 추가
    modals.forEach(({ modal, closeSpan }) => {
        closeSpan.onclick = () => closeModal(modal);
    });

    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        modals.forEach(({ modal }) => {
            if (event.target === modal) {
                closeModal(modal);
            }
        });
    };


    //// GET
    // Notice 테이블에서 클릭 이벤트 리스너 등록
    noticeTable.addEventListener('click', function(e) {
        if (e.target.tagName === 'TD') {
            const row = e.target.closest('tr');  // 클릭한 셀이 속한 행
            const noticeId = row.dataset.id;  // 데이터 세트에서 공지사항 ID 가져오기
            fetchNoticeDetails(noticeId);  // 공지사항 상세 정보를 가져오는 함수 호출
        }
    });

    // Notice 상세 정보 가져오기 함수
    function fetchNoticeDetails(noticeId) {
        fetch(`/api/notices/${noticeId}`)  // 해당 ID의 공지사항 정보 가져오기
            .then(response => response.json())
            .then(data => {
                // 모달에 공지사항 정보 출력
                document.getElementById('noticeDetailTitle').textContent = data.title;  // 제목
                document.getElementById('noticeDetailContent').textContent = data.contents;  // 내용
                document.getElementById('noticeDetailDate').textContent = `작성일: ${data.createDate}`;  // 작성일
                document.getElementById('noticeDetailAuthor').textContent = `작성자: ${data.employee.empName}`;  // 작성자
                noticeDetailModal.style.display = "block";  // 상세 모달 열기
            })
            .catch(error => console.error('Error:', error));  // 에러 처리
    }

    //// POST
    // Notice 작성 버튼 클릭 시 모달 열기 함수 불러오기
    document.querySelector('#createNoticeBtn').addEventListener('click', function(e) {
        e.preventDefault(); // 링크 기본 동작 방지
        openCreateNoticeModal(); // 모달 열기 함수 호출
    });

    // Notice 작성 모달열기
    function openCreateNoticeModal() {
        const createNoticeModal = document.getElementById('createNoticeModal');
        createNoticeModal.style.display = "block";
    }

    // Notice 작성 폼 제출 처리
    document.getElementById('createNoticeForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 폼 기본 동작 방지

        const title = document.getElementById('createNoticeTitle').value;
        const content = document.getElementById('createNoticeContent').value;
        const emp_num = 1;  ////////// 1번 사원 가데이터 입력

        fetch('/api/notices', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                contents: content,
                emp_num: emp_num
            })
        })
            .then(data => {
                alert('게시글이 작성되었습니다.');
                console.log('Response Data:', data); // 응답 데이터를 콘솔에 출력
                location.reload(); // 페이지 새로고침
            })
    });

    //// PUT
    // Notice 버튼 클릭 시 공지사항 정보 불러오기
    document.querySelector('#notice table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-edit')) {
            // 수정 버튼 클릭 시
            const noticeId = e.target.dataset.id;  // 데이터 세트에서 공지사항 ID 가져오기
            fetchNoticeEdit(noticeId);  // 공지사항 상세 정보를 가져오는 함수 호출
        }
    });

    // Notice 수정을 위한 공지사항 정보 가져오기 함수
    function fetchNoticeEdit(noticeId) {
        fetch(`/api/notices/${noticeId}`)  // 해당 ID의 공지사항 정보 가져오기
            .then(response => response.json())
            .then(data => {
                document.getElementById('editNoticeId').value = noticeId;  // ID (hidden)
                document.getElementById('editNoticeTitle').value = data.title;  // 제목
                document.getElementById('editNoticeContent').value = data.contents;  // 내용
                editNoticeModal.style.display = "block";  // 수정 모달 열기
            })
            .catch(error => console.error('Error:', error));  // 에러 처리
    }

    // Notice 수정 폼 제출 처리
    document.getElementById('editNoticeForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 폼 기본 동작 방지

        const noticeId = document.getElementById('editNoticeId').value;
        const title = document.getElementById('editNoticeTitle').value;
        const content = document.getElementById('editNoticeContent').value;
        const emp_num = 1;  ////////// 1번 사원 가데이터 입력

        fetch(`/api/notices/${noticeId}`, {
            method: 'PUT',  // 수정 요청을 위한 PUT 메서드 사용
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                contents: content,
                emp_num: emp_num
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('게시글이 수정되었습니다.');
                location.reload(); // 페이지 새로고침
            })
            .catch(error => console.error('Error:', error));
    });

    //// DELETE
    // Notice 삭제 버튼 클릭 시 공지사항 정보 불러오기
    document.querySelector('#notice table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-delete')) {
            // 삭제 버튼 클릭 시
            const noticeId = e.target.dataset.id;  // 데이터 세트에서 공지사항 ID 가져오기
            const userConfirmed = confirm('정말로 삭제하시겠습니까?');  // 사용자 확인

            if (userConfirmed) {
                fetchNoticeDelete(noticeId);  // 공지사항 삭제 함수 호출
            }
        }
    });

    // Notice 삭제를 위한 함수
    function fetchNoticeDelete(noticeId) {
        fetch(`/api/notices/${noticeId}`, {
            method: 'DELETE',  // 삭제 요청을 위한 DELETE 메서드 사용
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('게시글이 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            })
            .catch(error => console.error('Error:', error));
    }

    //// GET
    // Task 테이블에서 클릭 이벤트 리스너 등록
    taskTable.addEventListener('click', function(e) {
        if (e.target.tagName === 'TD') {
            const row = e.target.closest('tr');  // 클릭한 셀이 속한 행
            const taskId = row.dataset.id;  // 데이터 세트에서 작업 ID 가져오기
            fetchTaskDetails(taskId);  // 작업 상세 정보를 가져오는 함수 호출
        }
    });

    // Task 상세 정보 가져오기 함수
    function fetchTaskDetails(taskId) {
        fetch(`/api/tasks/${taskId}`)  // 해당 ID의 작업 정보 가져오기
            .then(response => response.json())
            .then(data => {
                // 모달에 작업 정보 출력
                document.getElementById('taskDetailContent').textContent = data.content;  // 내용
                document.getElementById('taskDetailAuthor').textContent = `작성자: ${data.employee.empName}`;  // 작성자
                taskDetailModal.style.display = "block";  // 상세 모달 열기
            })
            .catch(error => console.error('Error:', error));  // 에러 처리
    }


    //// POST
    // Task 작성 버튼 클릭 시 모달 열기 함수 불러오기
    document.querySelector('#createTaskBtn').addEventListener('click', function(e) {
        e.preventDefault(); // 링크 기본 동작 방지
        openCreateTaskModal(); // 모달 열기 함수 호출
    });

    // Task 작성 모달열기
    function openCreateTaskModal() {
        createTaskModal.style.display = "block";
    }

    // Task 작성 폼 제출 처리
    document.getElementById('createTaskForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 폼 기본 동작 방지

        const content = document.getElementById('createTaskContent').value;
        const emp_num = 1;  ////////// 1번 사원 가데이터 입력

        fetch('/api/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: content,
                emp_num: emp_num
            })
        })
            .then(data => {
                alert('작업이 작성되었습니다.');
                console.log('Response Data:', data); // 응답 데이터를 콘솔에 출력
                location.reload(); // 페이지 새로고침
            })
    });

    //// PUT
    // Task 수정 버튼 클릭 시 작업 정보 불러오기
    document.querySelector('#task table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-edit')) {
            // 수정 버튼 클릭 시
            const taskId = e.target.dataset.id;  // 데이터 세트에서 작업 ID 가져오기
            fetchTaskEdit(taskId);  // 작업 상세 정보를 가져오는 함수 호출
        }
    });

    // Task 수정을 위한 작업 정보 가져오기 함수
    function fetchTaskEdit(taskId) {
        fetch(`/api/tasks/${taskId}`)  // 해당 ID의 작업 정보 가져오기
            .then(response => response.json())
            .then(data => {
                document.getElementById('editTaskId').value = taskId;  // ID (hidden)
                document.getElementById('editTaskContent').value = data.content;  // 내용
                editTaskModal.style.display = "block";  // 수정 모달 열기
            })
            .catch(error => console.error('Error:', error));  // 에러 처리
    }

    // Task 수정 폼 제출 처리
    document.getElementById('editTaskForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 폼 기본 동작 방지

        const taskId = document.getElementById('editTaskId').value;
        const content = document.getElementById('editTaskContent').value;
        const emp_num = 1;  ////////// 1번 사원 가데이터 입력

        fetch(`/api/tasks/${taskId}`, {
            method: 'PUT',  // 수정 요청을 위한 PUT 메서드 사용
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                content: content,
                emp_num: emp_num
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('작업이 수정되었습니다.');
                location.reload(); // 페이지 새로고침
            })
            .catch(error => console.error('Error:', error));
    });

    //// DELETE
    // Task 삭제 버튼 클릭 시 작업 정보 불러오기
    document.querySelector('#task table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-delete')) {
            // 삭제 버튼 클릭 시
            const taskId = e.target.dataset.id;  // 데이터 세트에서 작업 ID 가져오기
            const userConfirmed = confirm('정말로 삭제하시겠습니까?');  // 사용자 확인

            if (userConfirmed) {
                fetchTaskDelete(taskId);  // 작업 삭제 함수 호출
            }
        }
    });

    // Task 삭제를 위한 함수
    function fetchTaskDelete(taskId) {
        fetch(`/api/tasks/${taskId}`, {
            method: 'DELETE',  // 삭제 요청을 위한 DELETE 메서드 사용
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('작업이 삭제되었습니다.');
                location.reload(); // 페이지 새로고침
            })
            .catch(error => console.error('Error:', error));
    }

});

//
//
// 시계
//
//

// 페이지가 로드될 때 updateTime 함수 실행
document.addEventListener('DOMContentLoaded', updateTime);

// 매초마다 updateTime 함수 실행
setInterval(updateTime, 1000);

// updateTime
function updateTime() {
    const currentTimeElement = document.getElementById('current-time');
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    currentTimeElement.textContent = `${hours}:${minutes}:${seconds}`;
}

//
//
// 출퇴근
//
//

// 페이지가 로드될 때 버튼 상태 초기화 함수 실행 (출퇴근버튼)
function initializeButtons() {
    // 서버에서 현재 근무 상태를 받아오는 부분
    fetch('/api/workTimes/getWorkStatus')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 응답을 파싱
        })
        .then(data => {
            const startWork = data.startWork; // 응답에서 startWork 값 추출

            const onWorkButton = document.getElementById('onWork-button');
            const offWorkButton = document.getElementById('offWork-button');

            if (startWork) {
                // 출근은 했지만 퇴근은 안 한 경우
                onWorkButton.style.opacity = '0';
                onWorkButton.style.visibility = 'hidden';
                offWorkButton.style.opacity = '1';
                offWorkButton.style.visibility = 'visible';
            } else {
                // 출근 및 퇴근 모두 했거나, 둘 다 안 한 경우
                onWorkButton.style.opacity = '1';
                onWorkButton.style.visibility = 'visible';
                offWorkButton.style.opacity = '0';
                offWorkButton.style.visibility = 'hidden';
            }
        })
        .catch(error => {
            console.error('근무 상태 가져오기 실패:', error);
        });
}

// 버튼 상태 토글 함수
function toggleButtons(action) {
    var onWorkButton = document.getElementById('onWork-button');
    var offWorkButton = document.getElementById('offWork-button');

    if (action === 'onWork') {
        onWorkButton.style.opacity = '0';
        onWorkButton.style.visibility = 'hidden';
        offWorkButton.style.opacity = '1';
        offWorkButton.style.visibility = 'visible';

        // 출근 요청 POST
        fetch('/api/workTimes/startWork', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text(); // 응답 본문을 텍스트로 가져온다
            })
            .then(text => {
                if (text) {
                    return JSON.parse(text); // 텍스트를 JSON으로 파싱
                }
                return {}; // 본문이 비어있다면 빈 객체 반환
            })
            .then(data => {
                console.log('출근 요청 성공:', data);
                location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('출근 요청 실패:', error);
            });

    } else if (action === 'offWork') {
        onWorkButton.style.opacity = '1';
        onWorkButton.style.visibility = 'visible';
        offWorkButton.style.opacity = '0';
        offWorkButton.style.visibility = 'hidden';

        // 퇴근 요청 POST
        fetch('/api/workTimes/endWork', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({})
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text(); // 응답 본문을 텍스트로 가져온다
            })
            .then(text => {
                if (text) {
                    return JSON.parse(text); // 텍스트를 JSON으로 파싱
                }
                return {}; // 본문이 비어있다면 빈 객체 반환
            })
            .then(data => {
                console.log('퇴근 요청 성공:', data);
                location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('퇴근 요청 실패:', error);
            });
    }
}

// 페이지 로드 시 버튼 상태 초기화
document.addEventListener('DOMContentLoaded', initializeButtons);

// 버튼 클릭 시 토글 처리 (onWork=출근)
document.getElementById('onWork-button').addEventListener('click', function() {
    toggleButtons('onWork');
});

// 버튼 클릭 시 토글 처리 (offWork=퇴근)
document.getElementById('offWork-button').addEventListener('click', function() {
    toggleButtons('offWork');
});

//
//
//
//
//
//




