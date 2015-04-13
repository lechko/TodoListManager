package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;


public class TodoListManagerActivity extends Activity {

    private ArrayList<Pair<String,Date>> items;
    private ListView todos;
    private final int REQ_CODE_ADD_NEW_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        items = new ArrayList<Pair<String, Date>>();

        registerForContextMenu(findViewById(R.id.lstTodoItems));
        TodoListAdapter itemsAdapter = new TodoListAdapter(this, items);

        todos = (ListView)findViewById(R.id.lstTodoItems);
        todos.setAdapter(itemsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lstTodoItems)
        {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            final String title = items.get(info.position).first;
            menu.setHeaderTitle(title);

            MenuItem menuItemDelete = menu.add("Delete Item");
            menuItemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    items.remove(info.position);
                    ((TodoListAdapter)todos.getAdapter()).notifyDataSetChanged();
                    return true;
                }
            });

            final String CALL_ITEM_PREFIX = "Call ";
            if (!title.startsWith(CALL_ITEM_PREFIX))
                return;

            MenuItem menuItemCall = menu.add(title);
            menuItemCall.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String phoneNumber = title.substring(CALL_ITEM_PREFIX.length());
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

    public void addItem()
    {
        Intent intent = new Intent(this, AddNewTodoItemActivity.class);
        startActivityForResult(intent, REQ_CODE_ADD_NEW_ITEM);
    }

    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_CANCELED)
            return;

        switch (reqCode) {
            case REQ_CODE_ADD_NEW_ITEM:
                String itemTitle = data.getStringExtra("item_title");
                Date dueDate = (Date) data.getSerializableExtra("due_date");

                items.add(new Pair<String, Date>(itemTitle, dueDate));
                ((TodoListAdapter)todos.getAdapter()).notifyDataSetChanged();
        }
    }
}
