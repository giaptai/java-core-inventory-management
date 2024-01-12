package work_w_files.services;

import work_w_files.models.Category;

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

    default void sfdf() {
        System.out.println();
    }

    static void saff() {
        System.out.println();
    }
}
