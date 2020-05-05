package utils.coding;

public enum CodeHelper {

    MESSAGE(1),
    INFORMATION(2),
    COMMUNICATION(3),
    C_ONLY(4),
    BASE_ONLY(5);


    private int code;

    CodeHelper(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
