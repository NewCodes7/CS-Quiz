function submitForm() {
    const formData = new FormData();
    formData.append("quizId", $("#quizId").val());

    $("[name^='userAnswers']").each(function (index) {
        formData.append(`userAnswers[${index}].userAnswer`, $(this).val());
    });

    $.ajax({
        url: "/quizzes/" + $("#quizId").val(),
        type: "POST",
        data: formData,
        processData: false,
        contentType: false
    })
        .done(function (fragment) {
            $('#answerResult').html(fragment);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            const errorMessage = jqXHR.responseText || "퀴즈 제출 중 오류가 발생했습니다.";
            alert(errorMessage);
        });
}