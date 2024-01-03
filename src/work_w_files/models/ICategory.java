package work_w_files.models;

import work_w_files.models.Category;

import java.util.List;

public interface ICategory {

    void inputData(List<Category> categories);
    abstract void displayData();
}
