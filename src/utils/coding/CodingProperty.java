package utils.coding;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CodingProperty {

    public static String encode(int codeHelper, String text) {
        String string = "";
        switch (codeHelper) {
            case 1:
                string = Base64.getEncoder().encodeToString(base_c(text, true, 7).getBytes(StandardCharsets.UTF_8));
                break;
            case 2:
                string = Base64.getEncoder().encodeToString(base_c(text, true, 10).getBytes(StandardCharsets.UTF_8));
                break;
            case 3:
                string = Base64.getEncoder().encodeToString(base_c(text, true, 8).getBytes(StandardCharsets.UTF_8));
                break;
            case 4:
                string = base_c(text, true, codeHelper);
                break;
            case 5:
                string = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
                break;

        }
        return string;

    }

    public static String decode(int codeHelper, String text) {
        String string = "";
        switch (codeHelper) {
            case 1:
                string = base_c(new String(Base64.getDecoder().decode(text)), false, 7);
                break;
            case 2:
                string = base_c(new String(Base64.getDecoder().decode(text)), false, 10);
                break;
            case 3:
                string = base_c(new String(Base64.getDecoder().decode(text)), false, 8);
                break;
            case 4:
                string = base_c(text, false, codeHelper);
                break;
            case 5:
                string = new String(Base64.getDecoder().decode(text));
                break;

        }
        return string;
    }

    private static String base_c(String text, boolean updown, int code) {
        int character;
        String coding = "";
        for (int i = 0; i < text.length(); i++) {
            character = text.charAt(i);
            if (updown) {
                character += code;
            } else {
                character -= code;
            }
            coding += (char) character;
        }
        return coding;
    }



}
