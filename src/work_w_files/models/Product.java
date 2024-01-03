package work_w_files.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product extends POJODefault<String, String, String, Boolean> implements IProduct {
    private double importPrice;
    private double exportPrice;
    private double profit;
    private int categoryId;
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) {
        this.exportPrice = exportPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Product() {
        super();
    }

    public Product(String id, String name, double importPrice, double exportPrice, String description, boolean status, int categoryId) {
        super(id, name, description, status);
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.profit = exportPrice - importPrice;
        this.categoryId = categoryId;
    }

//    public work_w_files.models.Product(String id, String name, String description, Boolean status, double importPrice, double exportPrice, double profit, int categoryId, LocalDateTime dateTime) {
//        super(id, name, description, status);
//        this.importPrice = importPrice;
//        this.exportPrice = exportPrice;
//        this.profit = profit;
//        this.categoryId = categoryId;
//        this.dateTime = dateTime;
//    }

    @Override
    public void inputData(List<Product> products, List<Category> categories) {
        Scanner sc = new Scanner(System.in);
        Pattern piD = Pattern.compile("^P[a-zA-Z0-9]{3}");
        Pattern pName = Pattern.compile("^[a-zA-Z-\\d\\s\\p{L}]{6,30}+$");
        Pattern pDescription = Pattern.compile("^[\\w\\s\\p{L}]+$");
        Pattern pStatus = Pattern.compile("^(true|false)+$");
        do {
            try {
                System.out.print("\nNhập id: ");
                String id = sc.nextLine();
                int checkId = products.stream().filter(item -> item.getId().equals(id)).toList().size(); // check duplicate id products
                Matcher mId = piD.matcher(id);
                if (checkId == 0 && mId.matches()) {
//                    super.setId(id);
                    this.setId(id);
                    break;
                } else throw new Exception("");
            } catch (Exception e) {
                System.err.println("gồm 4 kí tự, bắt đầu bằng “P”, duy nhất");
            }
        } while (true);
        do {
            System.out.print("\nNhập name: ");
            String name = sc.nextLine();
            Matcher mName = pName.matcher(name);
            if (mName.matches()) {
//                super.setName(name);
                this.setName(name);
                break;
            } else System.err.println("Duy nhất, từ 6-30 kí tự");
        } while (true);
        do {
            try {
                System.out.print("\nNhập importPrice: ");
                importPrice = Double.parseDouble(sc.nextLine());
                if (importPrice > 0) {
                    this.setImportPrice(importPrice);
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Số thực, lớn hơn 0");
            }
        } while (true);
        do {
            try {
                System.out.print("\nNhập exportPrice: ");
                exportPrice = Double.parseDouble(sc.nextLine());
                if (exportPrice > 0 && exportPrice > importPrice * MIN_INTEREST_RATE) {
                    this.setExportPrice(exportPrice);
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Phải lớn hơn 0 có giá trị\n" +
                        "lớn hơn giá nhập ít nhất MIN_INTEREST_RATE lần)");
            }
        } while (true);
//        this.setProfit(exportPrice - importPrice); // set profit
        calProfit(); // set profit
        do {
            System.out.print("\nNhập description: ");
            String description = sc.nextLine();
            Matcher mDescription = pDescription.matcher(description);
            if (mDescription.matches()) {
//                super.setDescription(description);
                this.setDescription(description);
                break;
            } else System.err.println("Không bỏ trống");
        } while (true);
        do {
            System.out.print("\nNhập status: ");
            String checkStatus = sc.nextLine();
            Matcher mStatus = pStatus.matcher(checkStatus);
            if (mStatus.matches()) {
                boolean status = Boolean.parseBoolean(checkStatus);
//                super.setStatus(status);
                this.setStatus(status);
                break;
            } else System.err.println("Chỉ nhận true/ false");
        } while (true);

        do {
            try {
                System.out.print("\nNhập categoryId: ");
                int categoryId = Integer.parseInt(sc.nextLine());
                long checkCategoryId = categories.stream().filter(category -> category.getId().equals(categoryId)).count();
                if (checkCategoryId > 0) {
                    this.setCategoryId(categoryId);
                    break;
                } else throw new Exception("");
            } catch (Exception e) {
                System.err.println("Số, phải nhập trong các giá trị mã danh mục đã lưu trước đó");
            }
        } while (true);

        this.setDateTime(LocalDateTime.now());
    }

    @Override
    public void displayData() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "work_w_files.models.Product{" +
                super.toString() +
                ", importPrice=" + importPrice +
                ", exportPrice=" + exportPrice +
                ", profit=" + profit +
                ", categoryId=" + categoryId +
                '}';
    }

    @Override
    public void calProfit() {
        profit = exportPrice - importPrice; // set profit
    }
}
