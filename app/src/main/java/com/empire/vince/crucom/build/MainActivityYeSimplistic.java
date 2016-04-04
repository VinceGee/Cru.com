package com.empire.vince.crucom.build;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.empire.vince.crucom.AppController;
import com.empire.vince.crucom.R;
import com.empire.vince.crucom.build.controller.NotificationHelper;
import com.empire.vince.crucom.build.controller.Preferences;
import com.empire.vince.crucom.build.db.DatabaseHelper;
import com.empire.vince.crucom.build.model.Task;
import com.empire.vince.crucom.build.view.EditTaskFragment;
import com.empire.vince.crucom.build.view.TasksFragment;
import com.empire.vince.crucom.dialogs.SweetAlertDialog;

import java.util.List;


import rx.Observer;
import rx.Subscription;

/**
 * Holds and controls the {@link TasksFragment} on smartphones. On tablets
 * it also displays and manages the {@link EditTaskFragment}.
 *
 * @author info@leoliebig.de
 */
public class MainActivityYeSimplistic extends AppCompatActivity implements TasksFragment.OnFragmentInteraction, EditTaskFragment.OnFragmentInteraction{

    private static final String TAG = MainActivityYeSimplistic.class.getSimpleName();

    //gui
    private TasksFragment tasksFragment;
    private EditTaskFragment editFragment;

    //misc
    private DatabaseHelper dbHelper;
    private List<Task> tasks;
    private boolean hideDoneTasks = false;
    //indicates whether the tasks were reordered and need to be updated
    private boolean updateTasks = false;

    //concurrency
    private Subscription subAllTasks;
    private Subscription subUpdateTasks;
    private FetchTasksObserver fetchTasksObserver;
    private UpdateTasksObserver updateTasksObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yemareminder);

        tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.tasks_fragment);
        editFragment = (EditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.edit_task_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        fetchTasksObserver = new FetchTasksObserver();
        updateTasksObserver = new UpdateTasksObserver();

        hideDoneTasks = Preferences.getDoneTasksHidden(getApplicationContext());
        tasksFragment.setDoneTasksHidden(hideDoneTasks);
    }


    @Override
    protected void onResume() {
        super.onResume();
        subAllTasks = dbHelper.getAllAsync(fetchTasksObserver);
    }

    @Override
    protected void onPause() {
        if(updateTasks) {
            if(AppController.DEBUG) Log.d(TAG, "Updating all tasks");
            subUpdateTasks = dbHelper.saveAllAsync(updateTasksObserver, tasks);
        }
        if(subAllTasks != null) subAllTasks.unsubscribe();
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_hide_done_tasks);
        if(item!=null) item.setChecked(hideDoneTasks);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCustomImage(R.mipmap.ic_launcher)
                    .setTitleText("Cru.com")
                    .setContentText("Are you sure you delete all tasks?")
                    .setCancelText("No,cancel please!")
                    .setConfirmText("Delete!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cru.com")
                                    .setContentText("You didn't delete all tasks")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            dbHelper.deleteAll();
                            tasks.clear();
                            tasksFragment.updateContent(tasks);
                        }
                    })
                    .show();

            return true;
        }
        else if(id == R.id.action_hide_done_tasks){
            item.setChecked(!item.isChecked());
            hideDoneTasks = item.isChecked();
            tasksFragment.setDoneTasksHidden(hideDoneTasks);
            Preferences.setDoneTasksHidden(getApplicationContext(), hideDoneTasks);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Lets the user create a new task and updates the GUI
     */
    private void createTask() {

        if (editFragment == null) {
            //smartphone layout, start a new activity for editing a task
            Intent intent = new Intent(MainActivityYeSimplistic.this, EditTaskActivity.class);
            intent.putExtra(AppController.INTENT_EXTRA_TASK_LIST_POSITION, tasks.size());
            startActivity(intent);
        }else{
            //tablet layout, update the editFragment
            Task newTask = new Task("");
            newTask.setListPosition(tasks.size());
            editFragment.setTask(newTask);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) actionBar.setTitle("New task");
        }
    }

    /**
     * Updates the the content of the editFragment if it is available
     * in the current layout (tablets only) by creating a new task.
     */
    private void updateEditFragmentContent() {
        //update the editFragment if it is visible
        if(editFragment != null){
            createTask();
        }
    }

    //TaskFragment callbacks

    @Override
    public void onTaskEdit(Task task) {

        if (editFragment == null) {
            //smartphone layout, start a new activity for editing a task
            Intent intent = new Intent(MainActivityYeSimplistic.this, EditTaskActivity.class);
            intent.putExtra(AppController.INTENT_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }else{
            //tablet layout, update the editFragment
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) actionBar.setTitle("Edit task");
            editFragment.setTask(task);
        }

    }

    @Override
    public void onTaskDone(Task task) {
        dbHelper.save(task);
        NotificationHelper.updateNotification(task, task.getId(), this);
    }

    @Override
    public void onTaskMoved(List<Task> tasksWithNewPositions) {
        tasks = tasksWithNewPositions;
        updateTasks = true;
    }

    //EditTaskFragment callbacks

    @Override
    public void onTaskChanged(@NonNull final Task task) {

        if(task.getId() == Task.TRANSIENT){
            Toast.makeText(this, getString(R.string.toast_created_task) + task.getTitle(), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, getString(R.string.toast_updated_task) + task.getTitle(), Toast.LENGTH_LONG).show();
        }
        long id = dbHelper.save(task);
        NotificationHelper.updateNotification(task, id, this);

        subAllTasks = dbHelper.getAllAsync(fetchTasksObserver);
    }

    @Override
    public void onTaskDeleted(@NonNull final Task task) {
        int result = dbHelper.deleteTask(task.getId());

        task.setReminder(null); //notifications for tasks without reminder dates will be deleted in the next line
        NotificationHelper.updateNotification(task, task.getId(), this);

        if (result != 1) Log.e(TAG, "Could not delete task: " + task.getTitle());

        subAllTasks = dbHelper.getAllAsync(fetchTasksObserver);
    }

    @Override
    public void onTaskUnchanged() {
        if(AppController.DEBUG) Log.d(TAG, "No changes on task");
    }

    /**
     * Observer for handling the result of an async database query for all
     * tasks performed via RxAndroid.
     */
    class FetchTasksObserver implements Observer<List<Task>>{

        @Override
        public void onCompleted() {
            tasksFragment.updateContent(tasks);
            if(AppController.DEBUG) Log.d(TAG, "Loaded " + tasks.size() + " task(s) from db");
            updateEditFragmentContent();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if(AppController.DEBUG) Log.e(TAG, "Error fetching all tasks from db: " + e.getLocalizedMessage());
        }

        @Override
        public void onNext(List<Task> tasks) {
            MainActivityYeSimplistic.this.tasks = tasks;
        }
    }

    /**
     * Observer for handling the result of an async database update query for all
     * tasks performed via RxAndroid.
     */
    class UpdateTasksObserver implements Observer<Boolean>{

        @Override
        public void onCompleted() {
            subUpdateTasks.unsubscribe();
            //reset the update flag
            updateTasks = false;
        }

        @Override
        public void onError(Throwable e) {
            if(AppController.DEBUG) Log.e(TAG, "Error updating all tasks from db: " + e.getLocalizedMessage());
        }

        @Override
        public void onNext(Boolean successful) {
            if(AppController.DEBUG){
                if(successful) Log.d(TAG, "Updated all tasks successfully");
                else Log.e(TAG, "Could not update tasks");
            }
        }
    }

}
