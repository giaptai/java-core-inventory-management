package work_w_files.view;

import work_w_files.services.CategoryService;
import work_w_files.services.ICategoryFile;
import work_w_files.services.ProductService;

import java.util.Scanner;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final ICategoryFile categoryFile = new CategoryService();
    private static final ProductService productFile = new ProductService();

    public static void UIStorageManager() {
        do {
            System.out.printf("%s\n", "====== QUẢN LÝ KHO ======");
            System.out.printf("%s", """
                    |  1. Quản lý danh mục  |
                    |  2. Quản lý sản phẩm  |
                    |  3. Thoát             |
                    =========================
                    """);
            System.out.print("Bạn chọn: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    UICategoryManager();
                    break;
                case 2:
                    UIProductManager();
                    break;
                case 3:
                    System.out.print("Are you sure (Y/N): ");
                    char c = sc.nextLine().charAt(0);
                    if (c == 'Y' || c == 'y') {
                        System.exit(3);
                    } else {
                        break;
                    }
                default:
                    System.out.println("Chọn lại");
                    break;
            }
        } while (true);
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void UICategoryManager() {
        int choice;
        do {
            System.out.printf("%s\n", "================ QUẢN LÝ DANH MỤC ================");
            System.out.printf("%s", """
                    | 1. Thêm mới danh mục                           |
                    | 2. Cập nhật danh mục                           |
                    | 3. Xóa danh mục                                |
                    | 4. Tìm kiếm danh mục theo tên danh mục         |
                    | 5. Thống kê số lượng sp đang có trong danh mục |
                    | 6. Quay lại                                    |
                    ================ QUẢN LÝ DANH MỤC ================
                    """);
            System.out.print("Bạn chọn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Bạn chọn: Thêm mới danh mục");
                    categoryFile.addToFile();
                    break;
                case 2:
                    System.out.println("Bạn chọn: Cập nhật danh mục");
                    categoryFile.updateFile();
                    break;
                case 3:
                    System.out.println("Bạn chọn: Xóa danh mục");
                    categoryFile.deteleFile();
                    break;
                case 4:
                    System.out.println("Bạn chọn: Tìm kiếm danh mục theo tên danh mục");
                    categoryFile.findByName();
                    break;
                case 5:
                    System.out.println("Bạn chọn: Thống kê số lượng sp đang có trong danh mục");
                    categoryFile.statisticsProducts();
                    break;
                case 6:
//                    UIStorageManager();
                    break;
                default:
                    System.err.println("Chọn lại");
                    break;
            }
        } while (choice != 6);
    }

    public static void UIProductManager() {
        int choice;
        do {
            System.out.printf("%s\n", "================= QUẢN LÝ SẢN PHẨM =================");
            System.out.printf("| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n%-1s\n%s",
                    "1. Thêm mới sản phẩm",
                    "2. Cập nhật sản phẩm",
                    "3. Xóa sản phẩm",
                    "4. Hiển thị sản phẩm theo tên A-Z",
                    "5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp",
                    "6. Tìm kiếm sản phẩm",
                    "7. Quay lại",
                    "================= ---------------- =================",
                    "Bạn chọn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Bạn chọn: Thêm mới sản phẩm");
                    productFile.addToFile();
                    break;
                case 2:
                    System.out.println("Bạn chọn: Cập nhật sản phẩm");
                    productFile.updateFile();
                    break;
                case 3:
                    System.out.println("Bạn chọn: Xóa sản phẩm");
                    productFile.deteleFile();
                    break;
                case 4:
                    System.out.println("Bạn chọn: Hiển thị sản phẩm theo tên A-Z");
                    productFile.showFromAtoZ();
                    break;
                case 5:
                    System.out.println("Bạn chọn: Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
                    productFile.showByProfitFromHighToLow();
                    break;
                case 6:
                    System.out.println("Bạn chọn: Tìm kiếm sản phẩm");
                    productFile.findProduct();
                    break;
                case 7:
//                    UIStorageManager();
                    break;
                default:
                    System.out.println("Chọn lại");
                    break;
            }
        } while (choice != 7);
    }
}
