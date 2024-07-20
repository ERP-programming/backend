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

    // 각 NOTICE 모달의 닫기 버튼 가져오기
    const noticeDetailCloseSpan = noticeDetailModal.querySelector('.close');
    const createNoticeCloseSpan = createNoticeModal.querySelector('.close');
    const editNoticeCloseSpan = editNoticeModal.querySelector('.close');

    //

    // 상세 모달의 닫기 버튼 클릭 이벤트 처리
    noticeDetailCloseSpan.onclick = function() {
        noticeDetailModal.style.display = "none";  // 상세 모달 숨기기
    };

    // 생성 모달의 닫기 버튼 클릭 이벤트 처리
    createNoticeCloseSpan.onclick = function() {
        createNoticeModal.style.display = "none";  // 생성 모달 숨기기
    };

    // 수정 모달의 닫기 버튼 클릭 이벤트 처리
    editNoticeCloseSpan.onclick = function() {
        editNoticeModal.style.display = "none";  // 수정 모달 숨기기
    };

    // 모달 외부 클릭 시 닫기 처리
    window.onclick = function(event) {
        if (event.target == noticeDetailModal) {
            noticeDetailModal.style.display = "none";  // 상세 모달 숨기기
        } else if (event.target == createNoticeModal) {
            createNoticeModal.style.display = "none";  // 생성 모달 숨기기
        } else if (event.target == editNoticeModal) {
            editNoticeModal.style.display = "none";  // 수정 모달 숨기기
        }
    };


    //// GET
    // 테이블에서 클릭 이벤트 리스너 등록
    noticeTable.addEventListener('click', function(e) {
        if (e.target.tagName === 'TD') {
            const row = e.target.closest('tr');  // 클릭한 셀이 속한 행
            const noticeId = row.dataset.id;  // 데이터 세트에서 공지사항 ID 가져오기
            console.log(noticeId);
            fetchNoticeDetails(noticeId);  // 공지사항 상세 정보를 가져오는 함수 호출
        }
    });

    // 공지사항 상세 정보 가져오기 함수
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
    // 게시글 작성 버튼 클릭 시 모달 열기 함수 불러오기
    document.querySelector('#createNoticeBtn').addEventListener('click', function(e) {
        e.preventDefault(); // 링크 기본 동작 방지
        openCreateNoticeModal(); // 모달 열기 함수 호출
    });

    // 작성 모달열기
    function openCreateNoticeModal() {
        const createNoticeModal = document.getElementById('createNoticeModal');
        createNoticeModal.style.display = "block";
    }

    // 게시글 작성 폼 제출 처리
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
    // 수정 버튼 클릭 시 공지사항 정보 불러오기
    document.querySelector('#notice table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-edit')) {
            // 수정 버튼 클릭 시
            const noticeId = e.target.dataset.id;  // 데이터 세트에서 공지사항 ID 가져오기
            fetchNoticeEdit(noticeId);  // 공지사항 상세 정보를 가져오는 함수 호출
        }
    });

    // 수정을 위한 공지사항 정보 가져오기 함수
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

    // 수정 폼 제출 처리
    document.getElementById('editNoticeForm').addEventListener('submit', function(e) {
        e.preventDefault(); // 폼 기본 동작 방지

        const noticeId = document.getElementById('editNoticeId').value;
        const title = document.getElementById('editNoticeTitle').value;
        const content = document.getElementById('editNoticeContent').value;
        const emp_num = 1;  ////////// 1번 사원 가데이터 입력

        /*
        // 로그로 데이터 확인
        console.log('Notice ID:', noticeId);
        console.log('Title:', title);
        console.log('Content:', content);
        console.log('Emp Num:', emp_num);
        */

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
    // 삭제 버튼 클릭 시 공지사항 정보 불러오기
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

    // 삭제를 위한 함수
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

});



// Task 모달 관련 기능
document.addEventListener('DOMContentLoaded', function() {
    // 필요한 TASK DOM 요소들 가져오기
    const taskTable = document.querySelector('#task table');  // 작업 목록 테이블
    const taskDetailModal = document.getElementById('taskDetailModal');  // 상세 모달
    const createTaskModal = document.getElementById('createTaskModal');  // 생성 모달
    const editTaskModal = document.getElementById('editTaskModal');  // 수정 모달

    // 각 TASK 모달의 닫기 버튼 가져오기
    const taskDetailCloseSpan = taskDetailModal.querySelector('.close');
    const createTaskCloseSpan = createTaskModal.querySelector('.close');
    const editTaskCloseSpan = editTaskModal.querySelector('.close');

    // 상세 모달의 닫기 버튼 클릭 이벤트 처리
    taskDetailCloseSpan.onclick = function() {
        taskDetailModal.style.display = "none";  // 상세 모달 숨기기
    };

    // 생성 모달의 닫기 버튼 클릭 이벤트 처리
    createTaskCloseSpan.onclick = function() {
        createTaskModal.style.display = "none";  // 생성 모달 숨기기
    };

    // 수정 모달의 닫기 버튼 클릭 이벤트 처리
    editTaskCloseSpan.onclick = function() {
        editTaskModal.style.display = "none";  // 수정 모달 숨기기
    };

    // 모달 외부 클릭 시 닫기 처리
    window.onclick = function(event) {
        if (event.target == taskDetailModal) {
            taskDetailModal.style.display = "none";  // 상세 모달 숨기기
        } else if (event.target == createTaskModal) {
            createTaskModal.style.display = "none";  // 생성 모달 숨기기
        } else if (event.target == editTaskModal) {
            editTaskModal.style.display = "none";  // 수정 모달 숨기기
        }
    };

    //// GET
    // 테이블에서 클릭 이벤트 리스너 등록
    taskTable.addEventListener('click', function(e) {
        if (e.target.tagName === 'TD') {
            const row = e.target.closest('tr');  // 클릭한 셀이 속한 행
            const taskId = row.dataset.id;  // 데이터 세트에서 작업 ID 가져오기
            fetchTaskDetails(taskId);  // 작업 상세 정보를 가져오는 함수 호출
        }
    });

    // 작업 상세 정보 가져오기 함수
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
    // 작업 작성 버튼 클릭 시 모달 열기 함수 불러오기
    document.querySelector('#createTaskBtn').addEventListener('click', function(e) {
        e.preventDefault(); // 링크 기본 동작 방지
        openCreateTaskModal(); // 모달 열기 함수 호출
    });

    // 모달열기
    function openCreateTaskModal() {
        createTaskModal.style.display = "block";
    }

    // 작업 작성 폼 제출 처리
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
    // 수정 버튼 클릭 시 작업 정보 불러오기
    document.querySelector('#task table').addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-edit')) {
            // 수정 버튼 클릭 시
            const taskId = e.target.dataset.id;  // 데이터 세트에서 작업 ID 가져오기
            console.log(taskId)
            fetchTaskEdit(taskId);  // 작업 상세 정보를 가져오는 함수 호출
        }
    });

    // 수정을 위한 작업 정보 가져오기 함수
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

    // 수정 폼 제출 처리
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
    // 삭제 버튼 클릭 시 작업 정보 불러오기
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

    // 삭제를 위한 함수
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







    ///////////////////////// timer
// 타이머 변수 선언
let timerInterval;
const workTimerDisplay = document.querySelector('.work-timer h1'); // 타이머를 표시할 요소
const startButton = document.getElementById('startButton'); // 출근 버튼
const endButton = document.getElementById('endButton'); // 퇴근 버튼

// 현재 시간을 기준으로 타이머 시작
function startTimer() {
    const startTime = new Date(); // 현재 시간 저장
    const endTime = localStorage.getItem('endTime'); // 마지막 종료 시간을 가져옴

    // 종료 시간이 설정되어 있으면
    if (endTime) {
        const endDate = new Date(endTime);
        if (endDate > startTime) {
            // 종료 시간이 아직 미래면 타이머를 다시 설정
            startTime.setTime(endDate.getTime() - (endDate.getTime() - startTime.getTime()));
        }
    }

    function updateTimer() {
        const now = new Date();
        const elapsedTime = now - startTime;

        const hours = Math.floor((elapsedTime / (1000 * 60 * 60)) % 24);
        const minutes = Math.floor((elapsedTime / (1000 * 60)) % 60);
        const seconds = Math.floor((elapsedTime / 1000) % 60);

        workTimerDisplay.textContent = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    }

    // 매초 타이머 업데이트
    timerInterval = setInterval(updateTimer, 1000);
    updateTimer(); // 즉시 업데이트
}

// 타이머 정지
function stopTimer() {
    clearInterval(timerInterval);
    localStorage.setItem('endTime', new Date()); // 종료 시간 저장
}

// 출근 버튼 클릭 이벤트
startButton.addEventListener('click', function() {
    if (!timerInterval) { // 타이머가 작동 중이 아니면
        startTimer();
        startButton.textContent = '퇴근하기'; // 버튼 텍스트 변경
    }
});

// 퇴근 버튼 클릭 이벤트
endButton.addEventListener('click', function() {
    if (timerInterval) { // 타이머가 작동 중이면
        stopTimer();
        endButton.textContent = '출근하기'; // 버튼 텍스트 변경
    }
});
