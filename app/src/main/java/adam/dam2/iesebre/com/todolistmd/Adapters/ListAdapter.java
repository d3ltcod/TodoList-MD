package adam.dam2.iesebre.com.todolistmd.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(final TodoListViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
