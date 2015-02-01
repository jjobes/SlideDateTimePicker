package com.github.jjobes.slidedatetimepicker;

import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Interface for the client to create a new SlideDateTimePicker.
 *
 * @author jjobes
 *
 */
public class SlideDateTimePicker
{
    public static final int HOLO_DARK = 1;
    public static final int HOLO_LIGHT = 2;

    private FragmentManager mFragmentManager;
    private SlideDateTimeListener mListener;
    private Date mInitialDate;
    private Date mMinDate;
    private Date mMaxDate;
    private boolean mIsClientSpecified24HourTime;
    private boolean mIs24HourTime;
    private int mTheme;
    private int mIndicatorColor;

    /**
     * Creates a new instance of SlideDateTimePicker.
     *
     * @param fm  The FragmentManager from the calling activity that is used
     *            internally to show the DialogFragment.
     */
    public SlideDateTimePicker(FragmentManager fm)
    {
        // See if there are any DialogFragments from the FragmentManager
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SlideDateTimeDialogFragment.TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT);

        // Remove if found
        if (prev != null)
        {
            ft.remove(prev);
            ft.commit();
        }

        mFragmentManager = fm;
    }

    /**
     * Sets the listener that is used to inform the client when
     * the user selects a new date and time. This must be called
     * before show().
     *
     * @param listener
     */
    public void setListener(SlideDateTimeListener listener)
    {
        mListener = listener;
    }

    /**
     * Sets the initial date and time to display in the date
     * and time pickers. If this method is not called, the
     * current date and time will be displayed.
     *
     * @param initialDate  the date object used to determine the
     *                     initial date and time to display
     */
    public void setInitialDate(Date initialDate)
    {
        mInitialDate = initialDate;
    }

    /**
     * Sets the minimum date that the DatePicker should show.
     * Should be called before show().
     *
     * @param minDate  the minimum selectable date for the DatePicker
     */
    public void setMinDate(Date minDate)
    {
        mMinDate = minDate;
    }

    /**
     * Sets the maximum date that the DatePicker should show.
     * Should be called before show().
     *
     * @param maxDate  the maximum selectable date for the DatePicker
     */
    public void setMaxDate(Date maxDate)
    {
        mMaxDate = maxDate;
    }

    private void setIsClientSpecified24HourTime(boolean isClientSpecified24HourTime)
    {
        mIsClientSpecified24HourTime = isClientSpecified24HourTime;
    }

    /**
     * Sets whether the TimePicker is in 12 hour (AM/PM) or 24 hour
     * mode. If this method is not called, the device's default
     * time format is used. This effects both the time displayed
     * in the tab and the TimePicker. Should be called before show().
     *
     * @param is24HourTime  <tt>true</tt> to force 24 hour time format,
     *                      <tt>false</tt> to force 12 hour (AM/PM) time
     *                      format.
     */
    public void setIs24HourTime(boolean is24HourTime)
    {
        setIsClientSpecified24HourTime(true);
        mIs24HourTime = is24HourTime;
    }

    /**
     * Sets the theme of the dialog. If no theme is specified, it
     * defaults to holo light.
     *
     * @param theme  SlideDateTimePicker.HOLO_DARK for a dark theme, or
     *               SlideDateTimePicker.HOLO_LIGHT for a light theme
     */
    public void setTheme(int theme)
    {
        mTheme = theme;
    }

    /**
     * Sets the color of the underline for the currently selected tab.
     *
     * @param indicatorColor  the color of the selected tab's underline
     */
    public void setIndicatorColor(int indicatorColor)
    {
        mIndicatorColor = indicatorColor;
    }

    /**
     * Shows the SlideDateTimeDialogFragment dialog. Make sure to
     * call setListener() before calling this.
     */
    public void show()
    {
        if (mListener == null)
        {
            throw new NullPointerException(
                    "Attempting to bind null listener to SlideDateTimePicker");
        }

        if (mInitialDate == null)
        {
            setInitialDate(new Date());
        }

        SlideDateTimeDialogFragment dialogFragment =
                SlideDateTimeDialogFragment.newInstance(
                        mListener,
                        mInitialDate,
                        mMinDate,
                        mMaxDate,
                        mIsClientSpecified24HourTime,
                        mIs24HourTime,
                        mTheme,
                        mIndicatorColor);

        dialogFragment.show(mFragmentManager,
                SlideDateTimeDialogFragment.TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT);
    }

    /*
     * The following implements the builder API.
     */
    public static class Builder
    {
        // Required
        private FragmentManager fm;
        private SlideDateTimeListener listener;

        // Optional
        private Date initialDate;
        private Date minDate;
        private Date maxDate;
        private boolean isClientSpecified24HourTime;
        private boolean is24HourTime;
        private int theme;
        private int indicatorColor;

        public Builder(FragmentManager fm)
        {
            this.fm = fm;
        }

        public Builder listener(SlideDateTimeListener listener)
        {
            this.listener = listener;
            return this;
        }

        public Builder initialDate(Date initialDate)
        {
            this.initialDate = initialDate;
            return this;
        }

        public Builder minDate(Date minDate)
        {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDate(Date maxDate)
        {
            this.maxDate = maxDate;
            return this;
        }

        public Builder is24HourTime(boolean is24HourTime)
        {
            this.isClientSpecified24HourTime = true;
            this.is24HourTime = is24HourTime;
            return this;
        }

        public Builder theme(int theme)
        {
            this.theme = theme;
            return this;
        }

        public Builder indicatorColor(int indicatorColor)
        {
            this.indicatorColor = indicatorColor;
            return this;
        }

        public SlideDateTimePicker build()
        {
            SlideDateTimePicker picker = new SlideDateTimePicker(fm);
            picker.setListener(listener);
            picker.setInitialDate(initialDate);
            picker.setMinDate(minDate);
            picker.setMaxDate(maxDate);
            picker.setIsClientSpecified24HourTime(isClientSpecified24HourTime);
            picker.setIs24HourTime(is24HourTime);
            picker.setTheme(theme);
            picker.setIndicatorColor(indicatorColor);

            return picker;
        }
    }
}
