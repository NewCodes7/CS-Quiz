// access token을 비공개 변수로 저장하기 위한
const authModule = (function() {
    let _accessToken = null;

    const setAccessToken = (token) => {
        _accessToken = token;
    };

    const getAccessToken = () => _accessToken;

    return {
        setAccessToken,
        getAccessToken
    };
})();