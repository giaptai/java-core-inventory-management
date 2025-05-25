package work_w_files.common.enums;

public enum Types {
    CATEGORY("DANH MỤC", "Thêm mới danh mục", "Cập nhật danh mục", "Xóa danh mục"),
    PRODUCT("SẢN PHẨM", "Thêm mới sản phẩm", "Cập nhật sản phẩm", "Xóa sản phẩm");

    private final String title;
    private final String ins;
    private final String upd;
    private final String del;

    private Types(String title, String ins, String upd, String del) {
        this.title = title;
        this.ins = ins;
        this.upd = upd;
        this.del = del;
    }

    public String getTitle() {
        return title;
    }

    public String getIns() {
        return ins;
    }

    public String getUpd() {
        return upd;
    }

    public String getDel() {
        return del;
    }

}
