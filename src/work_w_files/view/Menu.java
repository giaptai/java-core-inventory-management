package work_w_files.view;

import work_w_files.common.enums.Types;
import work_w_files.models.Category;
import work_w_files.models.Product;
import work_w_files.services.CategoryService;
import work_w_files.services.IWorkWithFile;
import work_w_files.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private final IWorkWithFile<Category> categoryService = new CategoryService();
    private final IWorkWithFile<Product> productService = new ProductService();

    public void UIStorageManager() {
        int choice;
        try {
            do {
                System.out.printf("%s\n", "====== QUẢN LÝ KHO ======");
                System.out.printf("%s", """
                        |  1. Quản lý danh mục  |
                        |  2. Quản lý sản phẩm  |
                        |  3. Thoát             |
                        =========================
                        """);
                System.out.print("Bạn chọn: ");
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        UITypesManager(Types.CATEGORY, categoryService);
                        break;
                    case 2:
                        UITypesManager(Types.PRODUCT, productService);
                        break;
                    case 3:
                        System.out.print("Bạn chắc không ? (Y/N): ");
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
        } catch (Exception e) {
            choice = 0;
        }
    }

    public <T> void UITypesManager(Types type, IWorkWithFile<T> service) {
        List<T> items = service.readFile().isEmpty() ? new ArrayList<>() : service.readFile();
        service.printAstable(items);

        int choice;
        do {
            System.out.printf("================ QUẢN LÝ %s ================\n", type.getTitle());
            System.out.printf("""
                    | 1. Thêm mới %s                           |
                    | 2. Cập nhật %s                           |
                    | 3. Xóa %s                                |
                    | 4. Tìm kiếm %s theo tên %s         |
                    | 5. Thống kê số lượng sp đang có trong %s |
                    | 6. Ghi Excel                                   |
                    | 7. Quay lại                                    |
                    ================ QUẢN LÝ %s ================
                    """, type.getTitle().toLowerCase(), type.getTitle().toLowerCase(), type.getTitle().toLowerCase(),
                    type.getTitle().toLowerCase(), type.getTitle().toLowerCase(), type.getTitle().toLowerCase(),
                    type.getTitle());
            System.out.print("Bạn chọn: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = 0;
            }

            switch (choice) {
                case 1:
                    System.out.printf("Bạn chọn: %s\n", type.getIns());
                    service.addToFile();
                    break;
                case 2:
                    System.out.printf("Bạn chọn: %s\n", type.getUpd());
                    service.updateFile();
                    break;
                case 3:
                    System.out.printf("Bạn chọn: %s\n", type.getDel());
                    service.deteleFile();
                    break;
                case 4:
                    System.out.printf("Bạn chọn: Tìm kiếm danh mục theo tên %s\n", type.getTitle().toLowerCase());
                    service.findByName();
                    break;
                case 5:
                    System.out.println("Bạn chọn: Thống kê số lượng sp đang có trong danh mục");
                    service.statisticsProducts();
                    break;
                case 6:
                    System.out.println("Bạn chọn: Ghi Excel");
                    service.writeExcel();
                    break;
                case 7:
                    return;
                default:
                    System.err.println("Chọn lại");
                    break;
            }
        } while (true);
    }

    // @SuppressWarnings({ "InfiniteLoopStatement" })
    // public static void UICategoryManager() {
    // int choice;
    // do {
    // System.out.printf("%s\n", "================ QUẢN LÝ DANH MỤC
    // ================");
    // System.out.printf("%s", """
    // | 1. Thêm mới danh mục |
    // | 2. Cập nhật danh mục |
    // | 3. Xóa danh mục |
    // | 4. Tìm kiếm danh mục theo tên danh mục |
    // | 5. Thống kê số lượng sp đang có trong danh mục |
    // | 6. Quay lại |
    // ================ QUẢN LÝ DANH MỤC ================
    // """);
    // System.out.print("Bạn chọn: ");
    // choice = Integer.parseInt(sc.nextLine());
    // switch (choice) {
    // case 1:
    // System.out.println("Bạn chọn: Thêm mới danh mục");
    // categoryFile.addToFile();
    // break;
    // case 2:
    // System.out.println("Bạn chọn: Cập nhật danh mục");
    // categoryFile.updateFile();
    // break;
    // case 3:
    // System.out.println("Bạn chọn: Xóa danh mục");
    // categoryFile.deteleFile();
    // break;
    // case 4:
    // System.out.println("Bạn chọn: Tìm kiếm danh mục theo tên danh mục");
    // categoryFile.findByName();
    // break;
    // case 5:
    // System.out.println("Bạn chọn: Thống kê số lượng sp đang có trong danh mục");
    // categoryFile.statisticsProducts();
    // break;
    // case 6:
    // // UIStorageManager();
    // break;
    // default:
    // System.err.println("Chọn lại");
    // break;
    // }
    // } while (choice != 6);
    // }

    // public static void UIProductManager() {
    // int choice;
    // do {
    // System.out.printf("%s\n", "================= QUẢN LÝ SẢN PHẨM
    // =================");
    // System.out.printf("| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n| %-48s |\n|
    // %-48s |\n| %-48s |\n%-1s\n%s",
    // "1. Thêm mới sản phẩm",
    // "2. Cập nhật sản phẩm",
    // "3. Xóa sản phẩm",
    // "4. Hiển thị sản phẩm theo tên A-Z",
    // "5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp",
    // "6. Tìm kiếm sản phẩm",
    // "7. Quay lại",
    // "================= ---------------- =================",
    // "Bạn chọn: ");
    // choice = Integer.parseInt(sc.nextLine());
    // switch (choice) {
    // case 1:
    // System.out.println("Bạn chọn: Thêm mới sản phẩm");
    // productFile.addToFile();
    // break;
    // case 2:
    // System.out.println("Bạn chọn: Cập nhật sản phẩm");
    // productFile.updateFile();
    // break;
    // case 3:
    // System.out.println("Bạn chọn: Xóa sản phẩm");
    // productFile.deteleFile();
    // break;
    // case 4:
    // System.out.println("Bạn chọn: Hiển thị sản phẩm theo tên A-Z");
    // productFile.showFromAtoZ();
    // break;
    // case 5:
    // System.out.println("Bạn chọn: Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
    // productFile.showByProfitFromHighToLow();
    // break;
    // case 6:
    // System.out.println("Bạn chọn: Tìm kiếm sản phẩm");
    // productFile.findProduct();
    // break;
    // case 7:
    // // UIStorageManager();
    // break;
    // default:
    // System.out.println("Chọn lại");
    // break;
    // }
    // } while (choice != 7);
    // }
}
