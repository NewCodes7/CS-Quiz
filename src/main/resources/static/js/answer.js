function submitForm() {
    var formData = {
        quizId: $("#quizId").val(), // 퀴즈 ID를 가져옴
        userAnswers: [] // 사용자 답변을 저장할 배열
    };

    // 각 답변 입력란의 값을 배열에 추가
    $("[name^='userAnswers']").each(function() {
        formData.userAnswers.push($(this).val());
    });

    var url = "/quizzes/" + quizId;

    $.ajax({
        url: url, // 퀴즈 ID를 포함한 URL로 요청을 보냄
        type: "POST",
        data: formData, // 폼 데이터 전송
    })
    .done(function (fragment) {
        $('#answerResult').html(fragment); // 결과를 #answerResult 요소에 업데이트
    });
}
