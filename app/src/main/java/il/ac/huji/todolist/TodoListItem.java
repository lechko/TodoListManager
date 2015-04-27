package il.ac.huji.todolist;

import java.util.Date;

public class TodoListItem {
    public long id;
    public String title;
    public Date dueDate;

    public TodoListItem(long id, String title, Date dueDate) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
    }
}
