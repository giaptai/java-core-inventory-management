package work_w_files.services;

import work_w_files.common.AppConstants;
import work_w_files.common.enums.Notification;
import work_w_files.models.Category;
import work_w_files.models.Product;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CategoryService implements IWorkWithFile<Category> {

    File file;
    private final IWorkWithFile<Product> productService;

    public CategoryService(File file, IWorkWithFile<Product> productService) {
        // this.file = file;
        this.productService = productService;
        //
        File dir = new File(AppConstants.PATHFILE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File fileProduct = new File(dir, "categories.txt");

        if (!fileProduct.exists()) {
            try {
                fileProduct.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = fileProduct;
        // this.productService = new work_w_files.services.ProductService(file, this);
    }

    public CategoryService() {
        File dir = new File(AppConstants.PATHFILE);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File fileProduct = new File(dir, "categories.txt");
        if (!fileProduct.exists()) {
            try {
                fileProduct.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = fileProduct;
        this.productService = new ProductService(file, this);
    }

    @Override
    public void writeToFile(List<Category> categories) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(categories);
            System.out.println("Save in file successfully \u001B[32m" + Notification.ADD_TO_FILE_SUCCESS + "\u001B[0m");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // hay bi loi java.lang.UnsupportedOperationException
    @Override
    public List<Category> readFile() {
        List<Category> categories = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            Object objInputStream = inputStream.readObject();
            if (objInputStream instanceof List<?>) {
                categories = (List<Category>) objInputStream;
            }
        } catch (EOFException e) {
            System.out.println("\u001B[33m"
                    + Notification.ADD_TO_FILE_SUCCESS
                    + "\u001B[0m");
        } catch (IOException e) {
            System.err.println("Loi khi doc file");
        } catch (ClassNotFoundException e) {
            // khi dịch chuyển class thì file lưu các dữ liệu lúc đó sẽ bị sai đường dẫn,
            // dẫn tới khi đọc file thì sẽ lỗi Class not found
            // throw new RuntimeException(e.getCause());
            // e.printStackTrace();
            System.err.println("\nLoi ClassNotFoundException");

        }
        // selection sort
        for (int i = 0; i < categories.size() - 1; i++) {
            int max = i; // auto select current index is max
            Category tempCategory = categories.get(i); // assign the temp work_w_files.models.Category
            for (int j = i + 1; j < categories.size(); j++) {
                if (categories.get(j).getId() > categories.get(max).getId()) {
                    max = j;
                }
            }
            categories.set(i, categories.get(max));
            categories.set(max, tempCategory);
        }
        return categories; // trả về immutable Collections ?????
    }

    @Override
    public void addToFile() {
        Scanner sc = new Scanner(System.in);
        List<Category> categories = (readFile().isEmpty() ? new ArrayList<>() : readFile()); // readd file
        int choice;
        do {
            System.out.println("Category:");
            printAstable(categories);
            Category category = new Category();
            category.inputData(categories);
            categories.add(category);
            System.out.print("Ban co muon nhap nua ko 1. Co 2. Khong: ");
            choice = Integer.parseInt(sc.nextLine());
        } while (choice != 2);
        writeToFile(categories);
    }

    @Override
    public void updateFile() {
        Scanner sc = new Scanner(System.in);
        try {
            List<Category> categories = readFile(); // đọc file
            System.out.println("Danh sách work_w_files.models.Category:");
            printAstable(categories);
            System.out.print("\nNhập id cần tìm: ");
            int categoryId = Integer.parseInt(sc.nextLine());
            System.out.printf("Tìm thấy: %d\n",
                    categories.stream().filter(category -> category.getId() == categoryId).count());
            long countDuplicate = categories.stream().filter(category -> category.getId() == categoryId).count();
            if (countDuplicate > 0) {
                for (Category category : categories) {
                    if (category.getId() == categoryId) {
                        category.inputData(categories);
                        break;
                    }
                }
                writeToFile(categories);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong input !!!");
        } finally {
        }
    }

    @Override
    public void deteleFile() {
        Scanner sc = new Scanner(System.in);
        try {
            List<Product> products = productService.readFile();
            List<Category> categories = readFile(); // readd file
            System.out.println("Danh sách work_w_files.models.Category:");
            printAstable(categories);
            System.out.print("\nNhập id danh mục cần xóa: ");
            int categoryId = Integer.parseInt(sc.nextLine());
            int countProductInCategory = products.stream().filter(product -> product.getCategoryId() == categoryId)
                    .toList().size();
            int countDuplicate = categories.stream().filter(category -> category.getId() == categoryId).toList().size();
            // using binary search
            if (countDuplicate > 0 & countProductInCategory == 0) {
                int left = 0;
                int high = categories.size() - 1;
                while (left <= high) {
                    int mid = left + (high - left) / 2;
                    if (categories.get(mid).getId() == categoryId) {
                        // categories.remove(mid);
                        categories.remove(categories.get(mid));
                        writeToFile(categories);
                        break;
                    }
                    if (categoryId > categories.get(mid).getId()) {
                        left = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
            } else
                System.err.printf("%s",
                        "Id not found or there is products belong to category, need remove products first");

        } catch (NumberFormatException e) {
            System.err.println("Wrong Input number at deteleFile !!!");
        } finally {
        }
    }

    @Override
    public void findByName() {
        Scanner sc = new Scanner(System.in);
        try {
            List<Category> categories = readFile(); // readd file
            System.out.println("Danh sách work_w_files.models.Category:");
            printAstable(categories);
            System.out.print("\nNhập Name danh mục cần tìm: ");
            String categoryName = sc.nextLine();
            // using binary search
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getName().toLowerCase().contains(categoryName.toLowerCase())) {
                    categories.get(i).displayData();
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong Input number at deteleFile !!!");
        } finally {
        }
    }

    @Override
    public void printAstable(List<Category> categories) {
        String line = "+------------+--------------------------------+--------------------------------+-----------------+";
        System.out.println(line);
        System.out.printf("| %-10s | %-30s | %-30s | %-15s |\n", "ID", "Name", "Description", "Status");
        System.out.println(line);
        for (Category category : categories) {
            System.out.printf("| %-10d | %-30s | %-30s | %-24s |%n",
                    category.getId(), category.getName(), category.getDescription(),
                    category.getStatus() ? Notification.HOAT_DONG.getS() : Notification.KHONG_HOAT_DONG.getS());
        }
        System.out.printf("%s\n", line);
    }

    @Override
    public void statisticsProducts() {
        try {
            List<Category> categories = readFile();
            List<Product> products = productService.readFile().isEmpty() ? new ArrayList<>()
                    : productService.readFile();
            long[] countSP = new long[categories.size()];
            System.out.println(
                    "+------------+--------------------------------+--------------------------------+-----------------+------------+");
            for (int i = 0; i < categories.size(); i++) {
                final int currentIndex = i;
                countSP[i] = products.stream()
                        .filter(product -> product.getCategoryId() == categories.get(currentIndex).getId()).count();
            }
            System.out.printf("| %1$-10s | %2$-30s | %3$-30s | %4$-15s | %5$-10s |\n", "ID", "Name", "Description",
                    "Status", "Products");
            System.out.println(
                    "+------------+--------------------------------+--------------------------------+-----------------+------------+");
            for (int i = 0; i < categories.size(); i++) {
                System.out.format("| %-10d | %-30s | %-30s | %-24s | %-10s |%n",
                        categories.get(i).getId(), categories.get(i).getName(), categories.get(i).getDescription(),
                        categories.get(i).getStatus() ? Notification.HOAT_DONG.getS()
                                : Notification.KHONG_HOAT_DONG.getS(),
                        countSP[i]);
            }
            System.out.printf("+%10s+%30s+%30s+%15s+%10s+\n", "------------", "--------------------------------",
                    "--------------------------------", "-----------------", "------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Category Data");

        int rowIndex = 0;

        // Ghi tiêu đề cột
        Row header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Status");
        List<Category> categories = readFile();
        // Ghi dữ liệu từng dòng
        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(String.valueOf(category.getId()));
            row.createCell(1).setCellValue(category.getName());
            row.createCell(2).setCellValue(category.getDescription() != null ? category.getDescription() : "");
            row.createCell(3).setCellValue(category.getStatus());
        }

        // Ghi file ra ổ đĩa
        try (FileOutputStream outputStream = new FileOutputStream(AppConstants.PATHFILE.concat("/CategoryData.xlsx"))) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Excel file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
