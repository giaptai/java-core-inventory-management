import java.util.List;

public interface IProduct {
    final float MIN_INTEREST_RATE = 0.2f;

    abstract void inputData(List<Product> products, List<Category> categories);

    void displayData();

    void calProfit();
}
