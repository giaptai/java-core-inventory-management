package work_w_files.services;

import work_w_files.enums.Notification;
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

public class CategoryService implements ICategoryFile {
    public static final String pathCategory = "data";
    File file;
    private final IWorkWithFile<Product> productService;

    public CategoryService(File file, IWorkWithFile<Product> productService) {
//        this.file = file;
        this.productService = productService;
        //
        File dir = new File(pathCategory);
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
//        this.productService = new work_w_files.services.ProductService(file, this);
    }

    public CategoryService() {
        File dir = new File(pathCategory);
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
            System.out.println("Save in file successfully");
            System.out.println("\u001B[32m"
                    + Notification.ADD_TO_FILE_SUCCESS
                    + "\u001B[0m");
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
            categories = (List<Category>) inputStream.readObject();
        } catch (EOFException e) {
            System.out.println("\u001B[33m"
                    + Notification.ADD_TO_FILE_SUCCESS
                    + "\u001B[0m");
        } catch (IOException e) {
            System.err.println("Loi khi doc file");
        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e.getCause());
            e.printStackTrace();
        }
        //selection sort
        for (int i = 0; i < categories.size() - 1; i++) {
            int max = i; // auto select current index is max
            Category tempCategory = categories.get(i); //  assign the temp work_w_files.models.Category
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
//        try {
        List<Category> categories = (readFile().isEmpty() ? new ArrayList<>() : readFile()); // readd file
        int choice;
        do {
            System.out.println("Danh sách work_w_files.models.Category:");
            printAstable(categories);
            Category category = new Category();
            category.inputData(categories);
            categories.add(category);
            System.out.print("Ban co muon nhap nua ko 1. Co 2. Khong: ");
            choice = Integer.parseInt(sc.nextLine());
        } while (choice != 2);
        writeToFile(categories);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
            System.out.printf("Tìm thấy: %d\n", categories.stream().filter(category -> category.getId() == categoryId).count());
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
            int countProductInCategory = products.stream().filter(product -> product.getCategoryId() == categoryId).toList().size();
            int countDuplicate = categories.stream().filter(category -> category.getId() == categoryId).toList().size();
            //using binary search
            if (countDuplicate > 0 & countProductInCategory == 0) {
                int left = 0;
                int high = categories.size() - 1;
                while (left <= high) {
                    int mid = left + (high - left) / 2;
                    if (categories.get(mid).getId() == categoryId) {
//                        categories.remove(mid);
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
                System.err.printf("%s", "Id not found or there is products belong to category, need remove products first");

        } catch (NumberFormatException e) {
            System.err.println("Wrong Input number at deteleFile !!!");
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
            //using binary search
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getName().toLowerCase().contains(categoryName.toLowerCase())) {
                    categories.get(i).displayData();
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong Input number at deteleFile !!!");
        }
    }

    private void printAstable(List<Category> categories) {
        System.out.printf("| %-10s | %-30s | %-30s | %-15s |\n", "ID", "Name", "Description", "Status");
        System.out.println("+------------+--------------------------------+--------------------------------+-----------------+");
        for (Category category : categories) {
            System.out.printf("| %-10d | %-30s | %-30s | %-24s |%n",
                    category.getId(), category.getName(), category.getDescription(), category.getStatus() ? Notification.HOAT_DONG.getS() : Notification.KHONG_HOAT_DONG.getS());
        }
        System.out.printf("+%10s+%30s+%30s+%15s+\n", "------------", "--------------------------------", "--------------------------------", "-----------------");
    }

    @Override
    public void statisticsProducts() {
        try {
            List<Category> categories = readFile();
            List<Product> products = productService.readFile().isEmpty() ? new ArrayList<>() : productService.readFile();
            long[] countSP = new long[categories.size()];
            System.out.println("+------------+--------------------------------+--------------------------------+-----------------+------------+");
            for (int i = 0; i < categories.size(); i++) {
                final int currentIndex = i;
                countSP[i] = products.stream().filter(product -> product.getCategoryId() == categories.get(currentIndex).getId()).count();
            }
            System.out.printf("| %1$-10s | %2$-30s | %3$-30s | %4$-15s | %5$-10s |\n", "ID", "Name", "Description", "Status", "Products");
            System.out.println("+------------+--------------------------------+--------------------------------+-----------------+------------+");
            for (int i = 0; i < categories.size(); i++) {
                System.out.format("| %-10d | %-30s | %-30s | %-24s | %-10s |%n",
                        categories.get(i).getId(), categories.get(i).getName(), categories.get(i).getDescription(), categories.get(i).getStatus() ? Notification.HOAT_DONG.getS() : Notification.KHONG_HOAT_DONG.getS(), countSP[i]);
            }
            System.out.printf("+%10s+%30s+%30s+%15s+%10s+\n", "------------", "--------------------------------", "--------------------------------", "-----------------", "------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
