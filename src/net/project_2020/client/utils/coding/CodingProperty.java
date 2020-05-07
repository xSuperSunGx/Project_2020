package net.project_2020.client.utils.coding;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CodingProperty {

    public static final String seperate = "noah_timo";

    public static synchronized String encode(int codeHelper, String text) {
        String string = "";
        switch (codeHelper) {
            case 1:
                string = base_c(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)), true, 2);
                break;
            case 2:
                string = base_c(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)), true, 10);
                break;
            case 3:
                string = base_c(Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)), true, 8);
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

    public static synchronized String decode(int codeHelper, String text) {
        String string = "";
        switch (codeHelper) {
            case 1:
                string = new String(Base64.getDecoder().decode(base_c(text, false, 2).getBytes(StandardCharsets.UTF_8)));
                break;
            case 2:
                string = new String(Base64.getDecoder().decode(base_c(text, false, 10).getBytes(StandardCharsets.UTF_8)));
                break;
            case 3:
                string = new String(Base64.getDecoder().decode(base_c(text, false, 8).getBytes(StandardCharsets.UTF_8)));
                break;
            case 4:
                string = base_c(text, false, codeHelper);
                break;
            case 5:
                string = new String(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)));
                break;

        }
        return string;
    }

    private static synchronized String base_c(String text, boolean updown, int code) {
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
