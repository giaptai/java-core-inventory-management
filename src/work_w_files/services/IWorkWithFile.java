package work_w_files.services;

import java.util.List;

public interface IWorkWithFile<T> {
    void writeToFile(List<T> ts);

    List<T> readFile();

    void addToFile();

    void updateFile();

    void deteleFile();

    default void findByName() {
    };

    default void statisticsProducts() {
    };

    void printAstable(List<T> t);

    default void writeExcel() {

    };
}
