package work_w_files.services;

import java.util.List;

public interface IWorkWithFile<T> {
    public void writeToFile(List<T> ts);
    public List<T> readFile();
    public void addToFile();
    public void updateFile();
    public void deteleFile();
}
