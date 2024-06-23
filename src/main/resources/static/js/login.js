// document.getElementById('loginForm').onsubmit = function(event) {
//     // 폼 제출 동작을 막음
//     event.preventDefault();
//
//     // CSRF 토큰 가져오기
//     const csrfParameterName = document.getElementById('csrfParameterName').value;
//     const csrfToken = document.getElementById('csrfToken').value;
//
//     // 이메일과 비밀번호 가져오기
//     const email = document.getElementById('email').value;
//     const password = document.getElementById('password').value;
//
//     // 폼 데이터 생성
//     const formData = new FormData();
//     formData.append(csrfParameterName, csrfToken);
//     formData.append('email', email);
//     formData.append('password_hashed', password);
//     console.log(formData);
//
//     // Fetch API를 사용하여 POST 요청 보내기
//     fetch('/login', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => {
//             if (response.ok) {
//                 return response.json();  // JSON 형식의 응답을 파싱
//             } else {
//                 throw new Error('Network response was not ok.');
//             }
//         })
//         .then(data => {
//             // 응답에서 accessToken 추출
//             const accessToken = data.access_token;  // 예시: 서버에서 accessToken을 JSON 형식으로 보내는 경우
//
//             // localStorage에 accessToken 저장
//             if (accessToken) {
//                 // authModule에도 설정
//                 authModule.setAccessToken(accessToken);
//             } else {
//                 console.error('accessToken not found in response');
//             }
//             // 로그인 성공 후 리다이렉트 등의 작업 수행
//             window.location.href = '/quizzes';
//
//         })
//         .catch(error => {
//             console.error('Fetch error:', error);
//             // 오류 처리
//             // 예: 사용자에게 오류 메시지 표시 등
//         });
// };