package adam.dam2.iesebre.com.todolistmd.Models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import adam.dam2.iesebre.com.todolistmd.R;

/**
 * Created by adam on 12/03/16.
 */
public class TodoListViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final TextView mContentView;
    public final CheckBox mDoneView;
    public TodoItem mItem;

    public TodoListViewHolder(View view) {
        super(view);
        mView = view;
        mContentView = (TextView) view.findViewById(R.id.todoListItemText);
        mDoneView = (CheckBox) view.findViewById(R.id.checkboxDone);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDoneView.isChecked() + mContentView.getText() + "'";
    }
}
