package com.empire.vince.crucom.build.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;



import java.util.Calendar;
import java.util.Date;


import com.empire.vince.crucom.AppController;
import com.empire.vince.crucom.build.model.Task;


/**
 * Show a dialog with a time picker.
 *
 * @author info@leoliebig.de
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnFragmentInteraction listener;
    private Task task;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        try {
            this.listener = (OnFragmentInteraction) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Hosting activity must implement a the OnFragmentInteraction interface");
        }

        final Calendar c = Calendar.getInstance();

        //init with the current values of the reminder time
        if(task == null) task = listener.getCurrentTask();
        if(task.getReminder() == null) c.setTime(AppController.getDefaultReminderDate());
        else c.setTime(task.getReminder());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //RefWatcher refWatcher = AppController.getRefWatcher(getParentFragment().getActivity());
        //refWatcher.watch(this);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Date reminder = task.getReminder();
        Calendar c = Calendar.getInstance();

        if(reminder != null){
            c.setTime(reminder);
        }
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        task.setReminder(c.getTime());
        listener.onTimeSet(task);
    }

    /**
     * Sets the task that should be edited.
     * @param taskToEdit The task to edit the time for.
     */
    public void setTask(@NonNull final Task taskToEdit){
        this.task = taskToEdit;
    }

    /**
     * Allows to interact with the {@link TimePickerFragment}.
     */
    public interface OnFragmentInteraction {

        /**
         * Is called if the user has picked a time.
         * @param task The {@link Task} with the updated time.
         */
        void onTimeSet(Task task);

        /**
         * Is called after an orientation change in order to get the current {@link Task}.
         * @return The current {@link Task} to edit.
         */
        Task getCurrentTask();

    }
}