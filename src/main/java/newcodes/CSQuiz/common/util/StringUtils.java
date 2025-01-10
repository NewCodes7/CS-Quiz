package newcodes.CSQuiz.common.util;

public class StringUtils {
    public static String normalize(String text) {
        return text.replaceAll("\\s+", "").toLowerCase();
    }
}
