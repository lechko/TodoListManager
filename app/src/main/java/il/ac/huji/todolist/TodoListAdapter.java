package il.ac.huji.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TodoListAdapter extends ArrayAdapter<TodoListItem> {
    private static class ViewHolder {
        private TextView itemTitle;
        private TextView dueDate;
    }

    public TodoListAdapter(Context context, ArrayList<TodoListItem> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.todo_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.dueDate = (TextView) convertView.findViewById(R.id.txtDueDate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemTitle.setTextColor(Color.GRAY);
        viewHolder.dueDate.setTextColor(Color.GRAY);

        TodoListItem item = getItem(position);
        if (item == null)
            return convertView;
        viewHolder.itemTitle.setText(item.title);
        if (item.dueDate == null)
        {
            viewHolder.dueDate.setText("No due date");
            return convertView;
        }

        viewHolder.dueDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(item.dueDate));

        if (item.dueDate.before(new GregorianCalendar().getTime())) {
            viewHolder.itemTitle.setTextColor(Color.RED);
            viewHolder.dueDate.setTextColor(Color.RED);
        }
        return convertView;
    }
}
