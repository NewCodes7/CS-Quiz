function submitForm(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    formData.append("quizId", $("#quiz-id").val());

    $.ajax({
        url: "/quizzes/" + $("#quiz-id").val(),
        type: "POST",
        data: formData,
        processData: false,
        contentType: false
    })
        .done(function (fragment) {
            $('#answerResult').html(fragment);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            // REFACTOR: 깔끔하게 예외 메시지만 받거나, 필터링하기
            const errorMessage = jqXHR.responseText || "퀴즈 제출 중 오류가 발생했습니다.";
            $('#answerResult').html(`<div class="alert">${errorMessage}</div>`);
        });
}