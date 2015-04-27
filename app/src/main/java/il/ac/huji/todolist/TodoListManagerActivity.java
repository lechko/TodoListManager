package il.ac.huji.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;


public class TodoListManagerActivity extends Activity {

    private ArrayList<TodoListItem> items;
    private ListView todos;
    private final int REQ_CODE_ADD_NEW_ITEM = 1;
    private TodoListDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new TodoListDBHelper(getApplicationContext());
        setContentView(R.layout.activity_todo_list_manager);

        items = new ArrayList<>();

        registerForContextMenu(findViewById(R.id.lstTodoItems));
        TodoListAdapter itemsAdapter = new TodoListAdapter(getApplicationContext(), items);

        todos = (ListView)findViewById(R.id.lstTodoItems);
        todos.setAdapter(itemsAdapter);

        fillFromDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    private void deleteFromDB(long itemID)
    {
        SQLiteDatabase todosDB = dbHelper.getReadableDatabase();
        todosDB.delete(
                TodoListContract.TodoListEntry.TABLE_NAME,
                TodoListContract.TodoListEntry._ID + " = ?",
                new String[]{Long.toString(itemID)});
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lstTodoItems)
        {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            final TodoListItem todoItem = items.get(info.position);
            menu.setHeaderTitle(todoItem.title);

            MenuItem menuItemDelete = menu.add("Delete Item");
            menuItemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deleteFromDB(items.get(info.position).id);
                    items.remove(info.position);
                    ((TodoListAdapter)todos.getAdapter()).notifyDataSetChanged();
                    return true;
                }
            });

            final String CALL_ITEM_PREFIX = "Call ";
            if (!todoItem.title.startsWith(CALL_ITEM_PREFIX))
                return;

            MenuItem menuItemCall = menu.add(todoItem.title);
            menuItemCall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String phoneNumber = todoItem.title.substring(CALL_ITEM_PREFIX.length());
                    Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(dial);
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.menuItemAdd:
                addItem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void fillFromDB()
    {
        SQLiteDatabase todosDB = dbHelper.getReadableDatabase();
        String[] projection = {
                TodoListContract.TodoListEntry._ID,
                TodoListContract.TodoListEntry.COLUMN_NAME_ITEM_TITLE,
                TodoListContract.TodoListEntry.COLUMN_NAME_DUE_DATE
        };

        Cursor c = todosDB.query(
                TodoListContract.TodoListEntry.TABLE_NAME,
                projection,
                null,null,null,null,null
                );

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String title = c.getString(1);
            Date dueDate = new Date();
            dueDate.setTime(c.getLong(2));
            items.add(new TodoListItem(id, title, dueDate));
        }
    }

    public void addItem()
    {
        Intent intent = new Intent(this, AddNewTodoItemActivity.class);
        startActivityForResult(intent, REQ_CODE_ADD_NEW_ITEM);
    }

    private long insertToDB(String title, Date dueDate)
    {
        SQLiteDatabase todosDB = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoListContract.TodoListEntry.COLUMN_NAME_ITEM_TITLE, title);
        values.put(TodoListContract.TodoListEntry.COLUMN_NAME_DUE_DATE, dueDate.getTime());
        return todosDB.insert(TodoListContract.TodoListEntry.TABLE_NAME, null, values);
    }

    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_CANCELED)
            return;

        switch (reqCode) {
            case REQ_CODE_ADD_NEW_ITEM:
                String itemTitle = data.getStringExtra("item_title");
                Date dueDate = (Date) data.getSerializableExtra("due_date");

                long id = insertToDB(itemTitle, dueDate);
                items.add(new TodoListItem(id, itemTitle, dueDate));
                ((TodoListAdapter)todos.getAdapter()).notifyDataSetChanged();
        }
    }
}
