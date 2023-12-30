import java.util.List;

public interface ICategoryFile extends IWorkWithFile<Category> {
    @Override
    void writeToFile(List<Category> categories);

    @Override
    List<Category> readFile();

    @Override
    void addToFile();

    @Override
    void updateFile();

    @Override
    void deteleFile();

    void findByName();

    void statisticsProducts();

}
