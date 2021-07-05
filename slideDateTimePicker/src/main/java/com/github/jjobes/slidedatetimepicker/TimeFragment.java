package com.github.jjobes.slidedatetimepicker;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

/**
 * The fragment for the second page in the ViewPager that holds
 * the TimePicker.
 *
 * @author jjobes
 *
 */
public class TimeFragment extends Fragment
{
    public static final String TIME_FRAGMENT_KEY = "200";

    private TimePicker mTimePicker;

    public TimeFragment() {
        // Required empty public constructor for fragment.
    }

    /**
     * Return an instance of TimeFragment with its bundle filled with the
     * constructor arguments. The values in the bundle are retrieved in
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} below to properly initialize the TimePicker.
     */
    public static TimeFragment newInstance(
            int theme,
            int hour,
            int minute,
            boolean isClientSpecified24HourTime,
            boolean is24HourTime
    ) {
        TimeFragment f = new TimeFragment();

        Bundle b = new Bundle();
        b.putInt("theme", theme);
        b.putInt("hour", hour);
        b.putInt("minute", minute);
        b.putBoolean("isClientSpecified24HourTime", isClientSpecified24HourTime);
        b.putBoolean("is24HourTime", is24HourTime);
        f.setArguments(b);

        return f;
    }

    /**
     * Create and return the user interface view for this fragment.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        assert getArguments() != null;
        int theme = getArguments().getInt("theme");
        int initialHour = getArguments().getInt("hour");
        int initialMinute = getArguments().getInt("minute");
        boolean isClientSpecified24HourTime = getArguments().getBoolean("isClientSpecified24HourTime");
        boolean is24HourTime = getArguments().getBoolean("is24HourTime");

        // Unless we inflate using a cloned inflater with a Holo theme,
        // on Lollipop devices the TimePicker will be the new-style
        // radial TimePicker, which is not what we want. So we will
        // clone the inflater that we're given but with our specified
        // theme, then inflate the layout with this new inflater.

        Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(),
                theme == SlideDateTimePicker.HOLO_DARK ?
                        android.R.style.Theme_Holo :
                        android.R.style.Theme_Holo_Light);

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View v = localInflater.inflate(R.layout.fragment_time, container, false);

        mTimePicker = (TimePicker) v.findViewById(R.id.timePicker);
        // block keyboard popping up on touch
        mTimePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        mTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Bundle result = new Bundle();
            result.putInt("hourOfDay", hourOfDay);
            result.putInt("minute", minute);
            getParentFragmentManager().setFragmentResult(TIME_FRAGMENT_KEY, result);
        });

        // If the client specifies a 24-hour time format, set it on
        // the TimePicker.
        if (isClientSpecified24HourTime)
        {
            mTimePicker.setIs24HourView(is24HourTime);
        }
        else
        {
            // If the client does not specify a 24-hour time format, use the
            // device default.
            mTimePicker.setIs24HourView(DateFormat.is24HourFormat(requireActivity()));
        }

        mTimePicker.setCurrentHour(initialHour);
        mTimePicker.setCurrentMinute(initialMinute);

        // Fix for the bug where a TimePicker's onTimeChanged() is not called when
        // the user toggles the AM/PM button. Only applies to 4.0.0 and 4.0.3.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        {
            fixTimePickerBug18982();
        }

        return v;
    }

    /**
     * Workaround for bug in Android TimePicker where the onTimeChanged() callback
     * is not invoked when the user toggles between AM/PM. But we need to be able
     * to detect this in order to dynamically update the tab title properly when
     * the user toggles between AM/PM.
     *
     * Registered as Issue 18982:
     *
     * https://code.google.com/p/android/issues/detail?id=18982
     */
    private void fixTimePickerBug18982()
    {
        View amPmView = ((ViewGroup) mTimePicker.getChildAt(0)).getChildAt(3);

        if (amPmView instanceof NumberPicker)
        {
            ((NumberPicker) amPmView).setOnValueChangedListener((picker, oldVal, newVal) -> {
                if (picker.getValue() == 1)  // PM
                {
                    if (mTimePicker.getCurrentHour() < 12)
                        mTimePicker.setCurrentHour(mTimePicker.getCurrentHour() + 12);
                }
                else  // AM
                {
                    if (mTimePicker.getCurrentHour() >= 12)
                        mTimePicker.setCurrentHour(mTimePicker.getCurrentHour() - 12);
                }

                Bundle result = new Bundle();
                result.putInt("hourOfDay", mTimePicker.getCurrentHour());
                result.putInt("minute", mTimePicker.getCurrentMinute());
                getParentFragmentManager().setFragmentResult("222", result);
            });
        }
    }
}