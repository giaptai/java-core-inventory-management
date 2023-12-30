import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProductService implements IWorkWithFile<Product> {
    public static final String pathProduct = "data";
    private final File file;
    private ICategoryFile category;

    public ProductService() {
//        this.category = new CategoryService();
        File file = new File(pathProduct);
        if (!file.exists()) {
            file.mkdir();
        }
        File fileProduct = new File("data/products.txt");
        if (!fileProduct.exists()) {
            try {
                fileProduct.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.file = fileProduct;
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
        } catch (EOFException e) {
            System.err.println("het dong");
        } catch (IOException e) {
            System.err.println("Loi khi doc file");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        products.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return products;
//        return products;
    }

    //add product in file
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
                products.add(product1);
                System.out.print("Bạn có muốn nhập thêm không? 1. Có 2. Không: ");
                choice = Integer.parseInt(sc.nextLine());
            } while (choice != 2);
            writeToFile(products);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //edit category in file
    @Override
    public void updateFile() {
        Scanner sc = new Scanner(System.in);
        List<Product> products = readFile(); // đọc file
        List<Category> categories = category.readFile();
        System.out.printf("%s", "Danh sách sản phẩm: \n");
        printAstable(products);
        System.out.print("Nhập id cần tìm: ");
        String productId = sc.nextLine();
        //dùng tìm kiếm nhị phân
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

    //xóa file
    @Override
    public void deteleFile() {
        Scanner sc = new Scanner(System.in);
        List<Product> products = readFile(); // readd file
        System.out.printf("%s", "Danh sách sản phẩm: \n");
        printAstable(products);
        System.out.print("Nhập id sản phẩm cần xóa: ");
        String productId = sc.nextLine();
        //using binary search
        int left = 0;
        int right = products.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (products.get(mid).getId().equals(productId)) {
                products.remove(mid);
                //writeToFile(products);
                break;
            }
            if (productId.compareTo(products.get(mid).getId()) > 0) {
                right = mid - 1;
            } else left = mid + 1;
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
//            products.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            printAstable(products);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void showByProfitFromHighToLow() {
        System.out.println("Hiển thị lợi nhuận từ cao - thấp: ");
        try {
            List<Product> products = readFile(); // readd file
            //comparator cho phép sắp xếp kiểu khác mặc định tại 1 thời diểm runtime
            products.sort((o1, o2) -> (int) (Math.round(o2.getProfit() * 100.0 / 100.0) - Math.round(o1.getProfit() * 100.0 / 100.0)));
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
            products = products.stream().filter(product -> product.getName().toLowerCase().contains(nameProduct.toLowerCase())).toList();
            System.out.println("List Products: ");
            printAstable(products);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAstable(List<Product> products) {
        System.out.printf("| %-4s | %-30s | %-12s | %-12s | %-12s | %-8s |\n", "ID", "Name", "Import Price", "Export Price", "Profit", "Category");
        System.out.println("+------+--------------------------------+--------------+--------------+--------------+----------+");
        for (Product product : products) {
            System.out.printf("| %-4s | %-30s | %-12.2f | %-12.2f | %-12.2f | %-8d |%n",
                    product.getId(), product.getName(), product.getImportPrice(), product.getExportPrice(), product.getProfit(), product.getCategoryId());
        }
        System.out.printf("+%4s+%30s+%12s+%12s+%12s+%8s+\n", "------", "--------------------------------", "--------------", "--------------", "--------------", "----------");
    }
}
