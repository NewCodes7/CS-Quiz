package newcodes.CSQuiz.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFormatter {

    public static String formatJson(String uglyJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // JSON 문자열을 Object로 파싱
            Object jsonObject = mapper.readValue(uglyJson, Object.class);

            // 다시 예쁘게 포맷팅된 JSON 문자열로 변환
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("JSON 포맷팅 중 오류 발생", e);
        }
    }
}