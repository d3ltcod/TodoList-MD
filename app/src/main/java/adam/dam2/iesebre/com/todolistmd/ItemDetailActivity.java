package adam.dam2.iesebre.com.todolistmd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import adam.dam2.iesebre.com.todolistmd.Models.TodoItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private int priority;
    private View positiveAction;
    private String taskName;
    private String taskDescription;
    private TodoItem itemSelected;
    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabedit = (FloatingActionButton) findViewById(R.id.fabedit);
        fabedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTask();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        //When stop app save tasks
        if (ItemListActivity.item_map == null) {
            return;
        }

        String tasksToSave = ItemListActivity.gson.toJson(ItemListActivity.item_map);

        SharedPreferences todos = getSharedPreferences(ItemListActivity.SHARED_PREFERENCES_TODOS, 0);
        SharedPreferences.Editor editor = todos.edit();
        editor.putString(ItemListActivity.TODO_LIST, tasksToSave);
        editor.apply();
        onBackPressed();
    }

    public void editTask() {

        final EditText taskNameText;
        final EditText taskDescriptionText;
        RadioGroup checkPriority;
        itemSelected = ItemListActivity.item_map.get(arguments.getString(ItemDetailFragment.ARG_ITEM_ID));
        taskName = itemSelected.name;
        taskDescription = itemSelected.description;

        MaterialDialog dialog = new MaterialDialog.Builder(this).
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

                        TodoItem todoItem = new TodoItem(taskName + taskDescription, taskName, false, priority, taskDescription);
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
}
