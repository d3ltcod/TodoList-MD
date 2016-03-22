package adam.dam2.iesebre.com.todolistmd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import adam.dam2.iesebre.com.todolistmd.Models.TodoItem;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    private static int priority;
    private static View positiveAction;
    private static String taskName;
    private static String taskDescription;
    private static Activity activity;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TodoItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ItemListActivity.item_map.get(getArguments().getString(ARG_ITEM_ID));

            activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.description);
        }

        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editTask(rootView, mItem);
                return true;
            }
        });

        return rootView;
    }

    public static void editTask(View v, final TodoItem itemSelected) {

        final EditText taskNameText;
        final EditText taskDescriptionText;
        RadioGroup checkPriority;
        taskName = itemSelected.name;
        taskDescription = itemSelected.description;

        MaterialDialog dialog = new MaterialDialog.Builder(v.getContext()).
                title("Update Task").
                customView(R.layout.form_add_task, true).
                negativeText("Cancel").
                positiveText("Update").
                negativeColor(Color.parseColor("#ff3333")).
                positiveColor(Color.parseColor("#2196F3")).
                onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        // Task priority
                        RadioGroup taskPriority = (RadioGroup) dialog.findViewById(R.id.task_priority);

                        switch (taskPriority.getCheckedRadioButtonId()) {
                            case R.id.task_priority_urgent:
                                priority = 1;
                                break;
                            case R.id.task_priority_important_not_urgent:
                                priority = 2;
                                break;
                            case R.id.task_priority_not_urgent:
                                priority = 3;
                                break;
                        }

                        TodoItem todoItem = new TodoItem(taskName + taskDescription, taskName, itemSelected.done, priority, taskDescription);
                        ItemListActivity.updateItem(itemSelected.id, todoItem);
                        save();
                    }
                }).


                build();

        dialog.show();

        taskNameText = (EditText) dialog.getCustomView().findViewById(R.id.task_tittle);
        taskDescriptionText = (EditText) dialog.getCustomView().findViewById(R.id.task_description);

        taskNameText.append(itemSelected.name);
        taskName = taskNameText.getText().toString();

        taskDescriptionText.append(itemSelected.description);
        taskDescription = taskDescriptionText.getText().toString();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false);

        taskNameText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskName = s.toString();
                positiveAction.setEnabled(taskName.trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taskDescriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskDescription = s.toString();
                positiveAction.setEnabled(taskDescription.trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkPriority = (RadioGroup) dialog.getCustomView().findViewById(R.id.task_priority);
        if (itemSelected.priority == 1){checkPriority.check(R.id.task_priority_urgent);}
        if (itemSelected.priority == 2){checkPriority.check(R.id.task_priority_important_not_urgent);}
        if (itemSelected.priority == 3){checkPriority.check(R.id.task_priority_not_urgent);}

        checkPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup taskPriority, int checkedId) {
                positiveAction.setEnabled(true);
            }
        });
    }

    private static void save(){
        //When stop save tasks
        if (ItemListActivity.item_map == null) {
            return;
        }

        String tasksToSave = ItemListActivity.gson.toJson(ItemListActivity.item_map);

        SharedPreferences todos = activity.getSharedPreferences(ItemListActivity.SHARED_PREFERENCES_TODOS, 0);
        SharedPreferences.Editor editor = todos.edit();
        editor.putString(ItemListActivity.TODO_LIST, tasksToSave);
        editor.apply();
        reload();
    }

    private  static void reload(){
        if (ItemListActivity.mTwoPane){activity.recreate();}
        else{activity.onBackPressed();}

    }
}