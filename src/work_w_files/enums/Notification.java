package work_w_files.enums;

public enum Notification {
    ADD_TO_FILE_SUCCESS(""),
    ADD_TO_FILE_FAIL(""),
    READ_FROM_FILE_SUCCESS(""),
    READ_FROM_FILE_FAIL(""),
    HOAT_DONG("\u001B[32mHOẠT ĐỘNG\u001B[0m"),
    KHONG_HOAT_DONG("\u001B[31mKHÔNG HOẠT ĐỘNG\u001B[0m"),
    ;

    private String s;

    private Notification(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

}
