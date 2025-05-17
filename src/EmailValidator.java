import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "mohammadkhalaf@gmail.com";

    public static boolean isValid(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
