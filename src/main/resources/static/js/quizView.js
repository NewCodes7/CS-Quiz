// 모든 체크박스 선택 해제
var checkboxes = document.querySelectorAll('.category-checkbox');
checkboxes.forEach(function(checkbox) {
    checkbox.checked = false;
});
var solvedTrueCheckbox = document.getElementById('solved-true-checkbox');
var solvedFalseCheckbox = document.getElementById('solved-false-checkbox');
solvedTrueCheckbox.checked = false;
solvedFalseCheckbox.checked = false;

// URL에서 모든 category, statuses 값 추출
var urlParams = new URLSearchParams(window.location.search);

if (urlParams.get('category') != null) {
    var categoryParams = urlParams.get('category').split(',');
    categoryParams.forEach(function(categoryParam) {
        checkboxes.forEach(function(checkbox) {
            if (checkbox.value == categoryParam) {
                checkbox.checked = true;
            }
        });
    });
}

if (urlParams.get('statuses') != null) {
    var solvedParams = urlParams.get('statuses').split(',');
    solvedParams.forEach(function(solvedParam) {
        if (solvedParam == 'solved') {
            solvedTrueCheckbox.checked = true;
        } else if (solvedParam == 'unsolved') {
            solvedFalseCheckbox.checked = true;
        }
    });
}

// 페이징 기능
const page_elements = document.getElementsByClassName("page-link");
Array.from(page_elements).forEach(function(element) {
    element.addEventListener('click', function() {
        document.getElementById('page').value = this.dataset.page;
        updateForm();
    });
});

// 검색 기능
const btn_search = document.getElementById("btn_search");
btn_search.addEventListener('click', function() {
    document.getElementById('kw').value = document.getElementById('search_kw').value;
    document.getElementById('page').value = 0;
    updateForm();
});

// 필터링(카테고리, statuses) 기능
function handleCheckboxChange() {
    var checkboxes = document.querySelectorAll('.category-checkbox');
    var solvedTrueCheckbox = document.getElementById('solved-true-checkbox');
    var solvedFalseCheckbox = document.getElementById('solved-false-checkbox');

    var solvedCheckboxStatus = {};
    var checkboxStatus = {};

    checkboxes.forEach(function(checkbox) {
        console.log(checkbox);
        checkboxStatus[checkbox.value] = checkbox.checked;
    });

    if (solvedTrueCheckbox.checked) {
        solvedCheckboxStatus['solved'] = 'solved';
    }

    if (solvedFalseCheckbox.checked) {
        solvedCheckboxStatus['unsolved'] = 'unsolved';
    }

    var statuses = [];
    for (var value in solvedCheckboxStatus) {
        statuses.push(value);
    }
    var statusesValue = statuses.join(','); // 쉼표로 구분된 카테고리 값

    var categories = [];
    for (var category in checkboxStatus) {
        if (checkboxStatus.hasOwnProperty(category) && checkboxStatus[category]) {
            categories.push(category);
        }
    }
    var categoryValue = categories.join(','); // 쉼표로 구분된 카테고리 값

    document.getElementById('statuses').value = statusesValue;
    document.getElementById('category').value = categoryValue;
    document.getElementById('page').value = 0;
    updateForm();
}

function updateForm() {
    const field = document.getElementById('kw');
    if (field.value == "") {
        field.remove();
    }

    const field2 = document.getElementById('category');
    if (field2.value == "none" || field2.value == "") {
        field2.remove();
    }

    const field3 = document.getElementById('statuses');
    if (field3.value == "none" || field3.value == "") {
        field3.remove();
    }

    document.getElementById('searchForm').submit();
}