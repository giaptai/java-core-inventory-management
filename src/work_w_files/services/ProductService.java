package work_w_files.services;

import work_w_files.common.AppConstants;
import work_w_files.models.Category;
import work_w_files.models.Product;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductService implements IWorkWithFile<Product> {
    private final File file;
    private final IWorkWithFile<Category> category;

    public ProductService(File file, IWorkWithFile<Category> category) {
        this.category = category;
        //
        File dir = new File(AppConstants.PATHFILE);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File fileProduct = new File(dir, "products.txt");
        if (!fileProduct.exists()) {
            try {
                fileProduct.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = fileProduct;
    }

    public ProductService() {
        File dir = new File(AppConstants.PATHFILE);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File fileProduct = new File("C:/Users/henta/IdeaProjects/rikei_BTL_002 - Copy/data/products.txt");
        if (!fileProduct.exists()) {
            try {
                fileProduct.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = fileProduct;
        this.category = new CategoryService(file, this);
    }

    // thêm vào file
    @Override
    public void writeToFile(List<Product> products) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(products);
            System.out.println("Save in file products.txt successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // doc file
    @Override
    public List<Product> readFile() {
        List<Product> products = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            products = (List<Product>) inputStream.readObject();

            // insertion sort
            for (int i = 1; i < products.size(); i++) {
                int j = i;
                Product currentProduct = products.get(i);
                while (j > 0 && currentProduct.getId().compareTo(products.get(j - 1).getId()) > 0) {
                    products.set(j, products.get(j - 1));
                    j--;
                }
                products.set(j, currentProduct);
            }
        } catch (EOFException e) {
            // e.printStackTrace();
            System.err.println("Hết dòng");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Loi khi doc file");
        } catch (ClassNotFoundException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
        products.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return products;
        // return products;
    }

    // add product in file
    @Override
    public void addToFile() {
        Scanner sc = new Scanner(System.in);
        try {
            List<Product> products = (readFile().isEmpty() ? new ArrayList<>() : readFile()); // readd file
            List<Category> categories = category.readFile();
            int choice;
            do {
                System.out.printf("%s", "Danh sách sản phẩm: \n");
                printAstable(products);
                Product product1 = new Product();
                product1.inputData(products, categories);
                // product1.setDateTime(LocalDateTime.now());
                products.add(product1);
                System.out.print("Bạn có muốn nhập thêm không? 1. Có 2. Không: ");
                choice = Integer.parseInt(sc.nextLine());
            } while (choice != 2);
            writeToFile(products);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
        }
    }

    // edit category in file
    @Override
    public void updateFile() {
        Scanner sc = new Scanner(System.in);
        List<Product> products = readFile(); // đọc file
        List<Category> categories = category.readFile();
        System.out.printf("%s", "Danh sách sản phẩm: \n");
        printAstable(products);
        System.out.print("Nhập id cần tìm: ");
        String productId = sc.nextLine();
        // dùng tìm kiếm nhị phân
        int left = 0;
        int high = products.size() - 1;
        while (left <= high) {
            int mid = left + (high - left) / 2;
            if (products.get(mid).getId().equals(productId)) {
                products.get(mid).inputData(products, categories);
                writeToFile(products);
                break;
            }
            if (productId.compareTo(products.get(mid).getId()) > 0) {
                high = mid - 1;
            } else
                left = mid + 1;
        }
    }

    // xóa file
    @Override
    public void deteleFile() {
        Scanner sc = new Scanner(System.in);
        List<Product> products = readFile(); // readd file
        System.out.printf("%s", "Danh sách sản phẩm: \n");
        printAstable(products);
        System.out.print("Nhập id sản phẩm cần xóa: ");
        String productId = sc.nextLine();
        // using binary search
        int left = 0;
        int right = products.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (products.get(mid).getId().equals(productId)) {
                products.remove(mid);
                // writeToFile(products);
                break;
            }
            if (productId.compareTo(products.get(mid).getId()) > 0) {
                right = mid - 1;
            } else
                left = mid + 1;
        }
    }

    public void showFromAtoZ() {
        try {
            System.out.print("Hiển thị danh sách từ A - Z: \n");
            List<Product> products = readFile(); // readd file
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            // products.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            printAstable(products);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void showByProfitFromHighToLow() {
        System.out.println("Hiển thị lợi nhuận từ cao - thấp: ");
        try {
            List<Product> products = readFile(); // readd file
            // comparator cho phép sắp xếp kiểu khác mặc định tại 1 thời diểm runtime
            products.sort((o1, o2) -> (int) (Math.round(o2.getProfit() * 100.0 / 100.0)
                    - Math.round(o1.getProfit() * 100.0 / 100.0)));
            printAstable(products);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void findProduct() {
        Scanner sc = new Scanner(System.in);
        List<Product> products = readFile();
        System.out.print("Tìm kiếm sản phẩm: ");
        try {
            String nameProduct = sc.nextLine();
            products = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(nameProduct.toLowerCase())).toList();
            System.out.println("List Products: ");
            printAstable(products);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }

    @Override
    public void printAstable(List<Product> products) {
        String line = "+------+--------------------------------+-----------------+-----------------+-----------------+-----------------+";
        List<Category> categories = category.readFile();
        String[] categoryName = new String[products.size()];
        Map<Integer, String> categoryIdToNameMap = new HashMap<>();
        for (Category value : categories) {
            String rawName = value.getName();
            String trimmed = rawName.length() > 15 ? rawName.substring(0, 12) + "..." : rawName;
            String formatted = String.format("\033[1;35m%-15s\u001B[0m", trimmed);
            categoryIdToNameMap.put(value.getId(), formatted);
        }
        System.out.println(line);
        for (int i = 0; i < products.size(); i++) {
            int categoryId = products.get(i).getCategoryId();
            categoryName[i] = categoryIdToNameMap.get(categoryId);
        }
        System.out.printf("| %-4s | %-30s | %-15s | %-15s | %-15s | %-15s |\n",
                "ID", "Name", "Import Price", "Export Price", "Profit", "Category");
        System.out.println(line);
        for (int i = 0; i < products.size(); i++) {
            System.out.format("| %-4s | %-30s | %,-15.2f | %,-15.2f | %,-15.2f | %-15s |%n",
                    products.get(i).getId(), products.get(i).getName(), products.get(i).getImportPrice(),
                    products.get(i).getExportPrice(), products.get(i).getProfit(), categoryName[i]);
        }
        System.out.printf("%s\n", line);
    }

    public void writeExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Product Data");

        int rowIndex = 0;

        // Ghi tiêu đề cột
        Row header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Description");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Import Price ");
        header.createCell(5).setCellValue("Export Price ");
        header.createCell(6).setCellValue("Profit ");
        header.createCell(7).setCellValue("Category ");
        List<Product> products = readFile();
        // Ghi dữ liệu từng dòng
        for (Product product : products) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(String.valueOf(product.getId()));
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDescription() != null ? product.getDescription() : "");
            row.createCell(3).setCellValue(product.getStatus());
            row.createCell(4).setCellValue(product.getImportPrice());
            row.createCell(5).setCellValue(product.getExportPrice());
            row.createCell(6).setCellValue(product.getProfit());
            row.createCell(7).setCellValue(product.getCategoryId());
        }
        // Ghi file ra ổ đĩa
        try (FileOutputStream outputStream = new FileOutputStream(AppConstants.PATHFILE.concat("/ProductData.xlsx"))) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Excel file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
