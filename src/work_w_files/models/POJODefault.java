package work_w_files.models;

import java.io.Serial;
import java.io.Serializable;

public class POJODefault<T1, T2, T3, T4> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private T1 id;
    private T2 name;
    private T3 description;
    private T4 status;

    public T1 getId() {
        return id;
    }

    public void setId(T1 id) {
        this.id = id;
    }

    public T2 getName() {
        return name;
    }

    public void setName(T2 name) {
        this.name = name;
    }

    public T3 getDescription() {
        return description;
    }

    public void setDescription(T3 description) {
        this.description = description;
    }

    public T4 getStatus() {
        return status;
    }

    public void setStatus(T4 status) {
        this.status = status;
    }

    public POJODefault() {
    }

    public POJODefault(T1 id, T2 name, T3 description, T4 status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "work_w_files.models.POJODefault{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", status=" + status +
                '}';
    }
}
