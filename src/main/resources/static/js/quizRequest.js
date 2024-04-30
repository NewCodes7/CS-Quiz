// 생성 기능
const createButton = document.getElementById('create-btn');

// 사용자가 입력한 대체 답변을 가져오는 함수
function getAlternativeAnswers(answer) {
    // 사용자가 입력한 대체 답변을 저장할 배열
    const alternativeAnswers = [];

    // 사용자 입력 필드에서 값 가져오는 부분
    const userInputFields = document.getElementById(answer).value;

    // 쉼표로 구분된 값을 배열로 변환
    const userInputValues = userInputFields.split(',');

    // 각 값을 반복하여 배열에 추가
    for (let value of userInputValues) {
        alternativeAnswers.push(value.trim()); // 값 앞뒤 공백 제거 후 추가
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

    const data = {
        quiz: {
            categoryId: parseInt(document.getElementById('categoryId').value),
            questionText: document.getElementById('questionText').value,
            difficulty: document.getElementById('difficulty').value,
            referenceUrl: referenceUrl,
            blankSentence: realBlankSentence
        },
        answers: answers.map(answer => ({
            answerText: answer,
            alternativeAnswers: getAlternativeAnswers(answer)
        }))
    };

    // API로 데이터를 전송하는 함수입니다.
    fetch('/api/quizzes', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    })
    .then(() => {
        alert('등록 완료되었습니다.');
        location.replace('/quizzes');
    })
    .catch(error => {
        console.error('등록 중 오류 발생:', error);
        // 오류가 발생했을 때 사용자에게 메시지를 표시하거나 다른 조치를 취할 수 있습니다.
    });
}

// createButton이 존재하고 클릭되었을 때 실행되는 이벤트 핸들러를 설정하는 함수입니다.
function setupCreateButtonEventListener() {
    const createButton = document.getElementById('create-btn');
    if (createButton) {
        createButton.addEventListener('click', event => {
            postDataToAPI();
        });
    }
}

// 함수 호출을 통해 기능을 실행합니다.
setupCreateButtonEventListener();

// {}로 감싸진 단어를 추출하는 함수
function extractWordsInBraces(text) {
    const regex = /{([^}]*)}/g;
    const matches = text.match(regex);
    return matches ? matches.map(match => match.replace('{', '').replace('}', '')) : [];
}

// 대체 답변을 입력받는 input 요소들을 동적으로 생성하는 함수
function createAlternativeAnswerInputs() {
    // 빈칸이 포함된 textarea 요소
    const blankSentenceTextarea = document.getElementById('blankSentence');
    // 빈칸이 포함된 문장에서 {}로 감싸진 단어 추출
    const wordsInBraces = extractWordsInBraces(blankSentenceTextarea.value);

    // 동적으로 input 요소 생성
    const inputsContainer = document.createElement('div');
    inputsContainer.id = 'alternativeAnswersContainer';

    // 각 단어에 대해 input 요소 생성
    wordsInBraces.forEach(word => {
        const input = document.createElement('input');
        input.type = 'text';
        input.className = 'form-control alternativeAnswerInput';
        input.id = `${word}`;
        input.placeholder = `${word}에 대한 대체 답변(,로 구분해서 입력)`;
        inputsContainer.appendChild(input);
    });

    // 이전에 생성된 input 요소들을 삭제
    const existingInputsContainer = document.getElementById('alternativeAnswersContainer');
    if (existingInputsContainer) {
        existingInputsContainer.remove();
    }

    // 생성된 input 요소들을 추가
    blankSentenceTextarea.parentNode.insertBefore(inputsContainer, blankSentenceTextarea.nextSibling);
}

// blankSentence 값이 변경될 때마다 대체 답변 input 요소를 업데이트하는 이벤트 리스너 추가
const blankSentenceTextarea = document.getElementById('blankSentence');
blankSentenceTextarea.addEventListener('input', createAlternativeAnswerInputs);

// 초기에 한 번 실행하여 화면 로딩 시 대체 답변 input 요소를 생성
createAlternativeAnswerInputs();
