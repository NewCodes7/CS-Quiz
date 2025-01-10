const createButton = document.getElementById('create-btn');

// 사용자가 입력한 대체 답변을 가져오는 함수
function getAlternativeAnswers(answer) {
    const alternativeAnswers = [];
    const userInputFields = document.getElementById(answer).value;
    const userInputValues = userInputFields.split(',');

    for (let value of userInputValues) {
        alternativeAnswers.push(value.trim());
    }

    return alternativeAnswers;
}

function postDataToAPI() {
    const referenceUrlValue = document.getElementById('referenceUrl').value;
    const referenceUrl = referenceUrlValue !== '' ? referenceUrlValue : '';

    const blankSentence = document.getElementById('blankSentence');
    const regex = /{([^}]*)}/g;
    const matches = blankSentence.value.match(regex);
    const answers = matches.map(match => match.replace('{', '').replace('}', ''));

    const realBlankSentence = blankSentence.value.replace(regex, '{}');

    const categoryId = document.getElementById('categoryId');
    const difficulty = document.getElementById('difficulty');

    const data = {
        quiz: {
            categoryId: parseInt(categoryId.options[categoryId.selectedIndex].value),
            questionText: document.getElementById('questionText').value,
            difficulty: difficulty.options[difficulty.selectedIndex].value,
            referenceUrl: referenceUrl,
            blankSentence: realBlankSentence
        },
        answers: answers.map(answer => ({
            answerText: answer,
            alternativeAnswers: getAlternativeAnswers(answer)
        }))
    };

    fetch('/api/quizzes', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    })
        .then(() => {
            alert('퀴즈 등록을 요청했습니다. 검토 후 업로드될 예정입니다.');
            location.replace('/quizzes');
        })
        .catch(error => {
            console.error('등록 중 오류 발생:', error);
            // 오류가 발생했을 때 사용자에게 메시지를 표시하거나 다른 조치를 취할 수 있습니다.
        });
}

function extractWordsInBraces(text) {
    const regex = /{([^}]*)}/g;
    const matches = text.match(regex);
    return matches ? matches.map(match => match.replace('{', '').replace('}', '')) : [];
}

function createAlternativeAnswerInputs() {
    const blankSentenceTextarea = document.getElementById('blankSentence');
    const wordsInBraces = extractWordsInBraces(blankSentenceTextarea.value);

    const inputsContainer = document.createElement('div');
    inputsContainer.id = 'alternativeAnswersContainer';

    wordsInBraces.forEach(word => {
        const input = document.createElement('input');
        input.type = 'text';
        input.className = 'form-control alternativeAnswerInput';
        input.id = `${word}`;
        input.placeholder = `${word}에 대한 대체 답변(,로 구분해서 입력)`;
        inputsContainer.appendChild(input);
    });

    const existingInputsContainer = document.getElementById('alternativeAnswersContainer');
    if (existingInputsContainer) {
        existingInputsContainer.remove();
    }

    blankSentenceTextarea.parentNode.insertBefore(inputsContainer, blankSentenceTextarea.nextSibling);
}

const blankSentenceTextarea = document.getElementById('blankSentence');
blankSentenceTextarea.addEventListener('input', createAlternativeAnswerInputs);

createAlternativeAnswerInputs();
