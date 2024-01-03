package work_w_files.models;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Category extends POJODefault<Integer, String, String, Boolean> implements ICategory {
    public Category() {
        super();
    }

    public Category(int id, String name, String description, boolean status) {
        super(id, name, description, status);
    }

    @Override
    public void inputData(List<Category> categories) {
        Scanner sc = new Scanner(System.in);
        Pattern piD = Pattern.compile("^[1-9]{1,}$");
        Pattern pName = Pattern.compile("^[a-zA-Z-\\d\\s\\p{L}]{6,30}+$");
        Pattern pDescription = Pattern.compile("^[\\w\\s\\p{L}]{1,300}+$");
        Pattern pStatus = Pattern.compile("^(true|false)+$");
        do {
            try {
                System.out.print("\nNhập id: ");
                int id = Integer.parseInt(sc.nextLine());
                Matcher mId = piD.matcher(Integer.toString(id));
                int checkId = categories.stream().filter(item -> item.getId().equals(id)).toList().size();
                if (mId.matches() && checkId == 0) {
//                    super.setId(id);
                    this.setId(id);
                    break;
                }else System.err.println("Phải là số nguyên lớn hơn 0, duy nhất");
//                else throw new NumberFormatException("");
            } catch (NumberFormatException e) {
                System.err.println("Phải là số nguyên lớn hơn 0, duy nhất");
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
            } else System.err.println("Không trùng nhau, từ 6-30 kí tự");
        } while (true);
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
    }

    @Override
    public void displayData() {
        System.out.println(this);
    }

    // remember when using constructor it java will create a instance of class which has instances field is auto initialize
    @Override
    public String toString() {
        return "CATEGORY{" +
                "id=" + this.getId() +
                ", name=" + this.getName() +
                ", description=" + this.getDescription() +
                ", status=" + this.getStatus() +
                '}';
    }
}
