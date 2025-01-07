package newcodes.CSQuiz.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFormatter {

    public static String formatJson(String uglyJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Object jsonObject = mapper.readValue(uglyJson, Object.class);

            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("JSON 포맷팅 중 오류 발생", e);
        }
    }
}