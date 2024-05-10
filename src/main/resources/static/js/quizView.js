const page_elements = document.getElementsByClassName("page-link");
Array.from(page_elements).forEach(function(element) {
    element.addEventListener('click', function() {
        document.getElementById('page').value = this.dataset.page;
        document.getElementById('searchForm').submit();
    });
});

const btn_search = document.getElementById("btn_search");
btn_search.addEventListener('click', function() {
    document.getElementById('kw').value = document.getElementById('search_kw').value;
    document.getElementById('page').value = 1;  // 검색버튼을 클릭할 경우 1페이지부터 조회한다.
    document.getElementById('searchForm').submit();
});

function toggleDropdown() {
    var categoryList = document.getElementById("category-list");
    if (categoryList.style.display === "none") {
        categoryList.style.display = "block";
    } else {
        categoryList.style.display = "none";
    }
}

function toggleSolvedDropdown() {
    var solvedDropdown = document.getElementById("solved-check-list");
    if (solvedDropdown.style.display === "none") {
        solvedDropdown.style.display = "block";
    } else {
        solvedDropdown.style.display = "none";
    }
}

var solvedCheckboxStatus = {};
var checkboxStatus = {};

// 체크박스 변경 시 호출되는 함수
function handleCheckboxChange() {
    var checkboxes = document.querySelectorAll('.category-checkbox');
    var solvedTrueCheckbox = document.getElementById('solved-true-checkbox');
    var solvedFalseCheckbox = document.getElementById('solved-false-checkbox');

    checkboxes.forEach(function(checkbox) {
        checkboxStatus[checkbox.value] = checkbox.checked;
    });

    if (solvedTrueCheckbox.checked) {
        solvedCheckboxStatus['solved'] = 'solved';
    }

    if (solvedFalseCheckbox.checked) {
        solvedCheckboxStatus['unsolved'] = 'unsolved';
    }

    // 저장된 정보를 폼에 담기
    updateForm();
}

// URL에서 모든 category 값 추출
var urlParams = new URLSearchParams(window.location.search);
var categoryParams = urlParams.get('category').split(',');
var solvedParams = urlParams.get('statuses').split(',');

// 모든 체크박스 선택 해제
var checkboxes = document.querySelectorAll('.category-checkbox');
checkboxes.forEach(function(checkbox) {
    checkbox.checked = false;
});

var solvedTrueCheckbox = document.getElementById('solved-true-checkbox');
var solvedFalseCheckbox = document.getElementById('solved-false-checkbox');
solvedTrueCheckbox.checked = false;
solvedFalseCheckbox.checked = false;

// 각 category 값과 일치하는 체크박스 선택
categoryParams.forEach(function(categoryParam) {
    checkboxes.forEach(function(checkbox) {
        if (checkbox.value == categoryParam) {
            checkbox.checked = true;
        }
    });
});

// solved 값에 따라 체크박스 선택
solvedParams.forEach(function(solvedParam) {
    if (solvedParam == 'solved') {
        solvedTrueCheckbox.checked = true;
    } else if (solvedParam == 'unsolved') {
        solvedFalseCheckbox.checked = true;
    }
});


// 폼 업데이트 함수
function updateForm() {
    var form = document.getElementById('searchForm');

    // 새로운 체크박스 추가
    var statuses = [];
    for (var value in solvedCheckboxStatus) {
        statuses.push(value);
    }
    var statusesValue = statuses.join(','); // 쉼표로 구분된 카테고리 값

    var input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'statuses';
    input.value = statusesValue;
    form.appendChild(input);

    // hidden input 엘리먼트를 이용하여 각 체크박스의 상태를 폼에 담기
    var categories = [];
    for (var category in checkboxStatus) {
        if (checkboxStatus.hasOwnProperty(category) && checkboxStatus[category]) {
            categories.push(category);
        }
    }
    var categoryValue = categories.join(','); // 쉼표로 구분된 카테고리 값

    var categoryInput = document.createElement('input');
    categoryInput.type = 'hidden';
    categoryInput.name = 'category';
    categoryInput.value = categoryValue;
    form.appendChild(categoryInput);

    document.getElementById('kw').value = document.getElementById('search_kw').value;
    document.getElementById('page').value = 1;
    document.getElementById('searchForm').submit();
}