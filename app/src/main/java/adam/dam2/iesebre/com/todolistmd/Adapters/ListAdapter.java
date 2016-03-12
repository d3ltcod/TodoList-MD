package adam.dam2.iesebre.com.todolistmd.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import adam.dam2.iesebre.com.todolistmd.ItemDetailActivity;
import adam.dam2.iesebre.com.todolistmd.ItemDetailFragment;
import adam.dam2.iesebre.com.todolistmd.ItemListActivity;
import adam.dam2.iesebre.com.todolistmd.Models.TodoItem;
import adam.dam2.iesebre.com.todolistmd.Models.TodoListViewHolder;
import adam.dam2.iesebre.com.todolistmd.R;

/**
 * Created by adam on 12/03/16.
 */
public class ListAdapter extends RecyclerView.Adapter<TodoListViewHolder> {
    private final List<TodoItem> mValues;
    private final FragmentActivity activity;
    int urgentColor = Color.parseColor("#fe0000");
    int mediumColor = Color.parseColor("#0099ff");
    int notUrgentColor = Color.parseColor("#00ff19");

    public ListAdapter(List<TodoItem> items, Activity activity) {
        mValues = items;
        this.activity =(FragmentActivity) activity;
    }

    @Override
    public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoListViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);
        holder.mDoneView.setChecked(mValues.get(position).done);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (mValues.get(position).priority){
                case 1: holder.mDoneView.setButtonTintList(ColorStateList.valueOf(urgentColor));
                    break;
                case 2: holder.mDoneView.setButtonTintList(ColorStateList.valueOf(mediumColor));
                    break;
                case 3: holder.mDoneView.setButtonTintList(ColorStateList.valueOf(notUrgentColor));
                    break;
            }
        } else {
            switch (mValues.get(position).priority){
                case 1: holder.mDoneView.setBackgroundColor(urgentColor);
                    break;
                case 2: holder.mDoneView.setBackgroundColor(mediumColor);
                    break;
                case 3: holder.mDoneView.setBackgroundColor(notUrgentColor);
                    break;
            }
        }

        showDone(holder.mContentView, holder.mDoneView, position);

        holder.mDoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDone(holder.mContentView, holder.mDoneView, position);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ItemListActivity.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });
    }

    private void showDone(TextView tv, CheckBox done, int position)
    {
        if (!done.isChecked()) {
            done.setChecked(false);
            mValues.get(position).done = false;
            tv.setPaintFlags(0);
        } else {
            done.setChecked(true);
            mValues.get(position).done = true;
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
