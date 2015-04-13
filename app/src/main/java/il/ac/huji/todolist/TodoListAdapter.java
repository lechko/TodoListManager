package il.ac.huji.todolist;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoListAdapter extends ArrayAdapter<Pair<String, Date>> {
    private static class ViewHolder {
        private TextView itemTitle;
        private TextView dueDate;
        private int defaultColor;
    }

    public TodoListAdapter(Context context, ArrayList<Pair<String, Date>> items) {
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
            viewHolder.defaultColor = viewHolder.itemTitle.getTextColors().getDefaultColor();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.itemTitle.setTextColor(viewHolder.defaultColor);
            viewHolder.dueDate.setTextColor(viewHolder.defaultColor);
        }

        Pair<String, Date> item = getItem(position);
        if (item == null)
            return convertView;
        viewHolder.itemTitle.setText(item.first);
        if (item.second == null)
        {
            viewHolder.dueDate.setText("No due date");
            return convertView;
        }

        viewHolder.dueDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(item.second));

        if (item.second.before(new GregorianCalendar().getTime())) {
            viewHolder.itemTitle.setTextColor(Color.RED);
            viewHolder.dueDate.setTextColor(Color.RED);
        }
        return convertView;
    }
}
