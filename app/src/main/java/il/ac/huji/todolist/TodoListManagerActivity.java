package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class TodoListManagerActivity extends Activity {

    private ArrayList<String> items;
    private ListView todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);

        items = new ArrayList<>();

        registerForContextMenu(findViewById(R.id.lstTodoItems));
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setTextColor(position % 2 == 0 ? Color.RED : Color.BLUE);

                return view;
            }
        };
        todos = (ListView)findViewById(R.id.lstTodoItems);
        todos.setAdapter(itemsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lstTodoItems)
        {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(items.get(info.position));

            MenuItem menuItemDelete = menu.add("Delete Item");
            menuItemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    items.remove(info.position);
                    ((ArrayAdapter<String>)todos.getAdapter()).notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        EditText inputTextbox = (EditText)findViewById(R.id.edtNewItem);
        items.add(inputTextbox.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(inputTextbox.getWindowToken(), 0);

        inputTextbox.getText().clear();
    }
}
