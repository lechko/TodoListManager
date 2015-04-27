package il.ac.huji.todolist;

import android.provider.BaseColumns;

public final class TodoListContract {
    public TodoListContract() {}

    public static abstract class TodoListEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ITEM_TITLE = "item_title";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
    }
}
