package work_w_files.models;

import java.util.List;

public interface IProduct {

    void inputData(List<Product> products, List<Category> categories);

    void displayData();

    void calProfit();
}
