package com.github.jjobes.slidedatetimepicker;

import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;

/**
 * The fragment for the first page in the ViewPager that holds
 * the {@link CustomDatePicker}.
 *
 * @author jjobes
 *
 */
public class DateFragment extends Fragment
{
    public static final String DATE_FRAGMENT_KEY = "100";

    private CustomDatePicker mDatePicker;

    public DateFragment()
    {
        // Required empty public constructor for fragment.
    }

    /**
     * Return an instance of DateFragment with its bundle filled with the
     * constructor arguments. The values in the bundle are retrieved in
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} below to properly initialize the DatePicker.
     */
    public static DateFragment newInstance(
            int theme,
            int year,
            int month,
            int day,
            Date minDate,
            Date maxDate
    ) {
        DateFragment f = new DateFragment();

        Bundle b = new Bundle();
        b.putInt("theme", theme);
        b.putInt("year", year);
        b.putInt("month", month);
        b.putInt("day", day);
        b.putSerializable("minDate", minDate);
        b.putSerializable("maxDate", maxDate);
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
        int initialYear = getArguments().getInt("year");
        int initialMonth = getArguments().getInt("month");
        int initialDay = getArguments().getInt("day");
        Date minDate = (Date) getArguments().getSerializable("minDate");
        Date maxDate = (Date) getArguments().getSerializable("maxDate");

        // Unless we inflate using a cloned inflater with a Holo theme,
        // on Lollipop devices the DatePicker will be the new-style
        // DatePicker, which is not what we want. So we will
        // clone the inflater that we're given but with our specified
        // theme, then inflate the layout with this new inflater.

        Context contextThemeWrapper = new ContextThemeWrapper(
                getActivity(),
                theme == SlideDateTimePicker.HOLO_DARK ?
                        android.R.style.Theme_Holo :
                        android.R.style.Theme_Holo_Light);

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View v = localInflater.inflate(R.layout.fragment_date, container, false);

        mDatePicker = (CustomDatePicker) v.findViewById(R.id.datePicker);
        // block keyboard popping up on touch
        mDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        mDatePicker.init(
                initialYear,
                initialMonth,
                initialDay,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Bundle result = new Bundle();
                    result.putInt("year", year);
                    result.putInt("monthOfYear", monthOfYear);
                    result.putInt("dayOfMonth", dayOfMonth);
                    getParentFragmentManager().setFragmentResult(DATE_FRAGMENT_KEY, result);
                });

        if (minDate != null)
            mDatePicker.setMinDate(minDate.getTime());

        if (maxDate != null)
            mDatePicker.setMaxDate(maxDate.getTime());

        return v;
    }
}