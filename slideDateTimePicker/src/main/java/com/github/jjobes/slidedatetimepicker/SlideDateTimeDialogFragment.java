package com.github.jjobes.slidedatetimepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import static com.github.jjobes.slidedatetimepicker.SlideDateTimePicker.*;

/**
 * <p>The {@code DialogFragment} that contains the {@link SlidingTabLayout}
 * and {@link CustomViewPager}.</p>
 *
 * <p>The {@code CustomViewPager} contains the {@link DateFragment} and {@link TimeFragment}.</p>
 *
 * <p>This {@code DialogFragment} is managed by {@link SlideDateTimePicker}.</p>
 *
 * @author jjobes
 *
 */
public class SlideDateTimeDialogFragment extends DialogFragment implements DateFragment.DateChangedListener,
                                                                           TimeFragment.TimeChangedListener
{
    public static final String TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT = "tagSlideDateTimeDialogFragment";
    private static SlideDateTimeListener mListener;

    private Context mContext;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private View mButtonHorizontalDivider;
    private View mButtonVerticalDivider;
    private Button mOkButton;
    private Button mCancelButton;
    private Date mInitialDate;
    private int mTheme;
    private int mIndicatorColor;
    private Date mMinDate;
    private Date mMaxDate;
    private boolean mIsClientSpecified24HourTime;
    private boolean mIs24HourTime;
    private DefaultSelector mDefaultDateSelector;

    private Calendar mCalendar;
    private int mDateFlags =
        DateUtils.FORMAT_SHOW_WEEKDAY |
        DateUtils.FORMAT_SHOW_DATE |
        DateUtils.FORMAT_ABBREV_ALL;

    public SlideDateTimeDialogFragment()
    {
        // Required empty public constructor
    }

    /**
     * <p>Return a new instance of {@code SlideDateTimeDialogFragment} with its bundle
     * filled with the incoming arguments.</p>
     *
     * <p>Called by {@link SlideDateTimePicker#show()}.</p>
     *
     * @param listener
     * @param initialDate
     * @param minDate
     * @param maxDate
     * @param isClientSpecified24HourTime
     * @param is24HourTime
     * @param defaultDateSelctor
     * @param theme
     * @param indicatorColor
     * @return
     */
    public static SlideDateTimeDialogFragment newInstance(SlideDateTimeListener listener,
                                                          Date initialDate, Date minDate, Date maxDate, boolean isClientSpecified24HourTime,
                                                          boolean is24HourTime, DefaultSelector defaultDateSelctor, int theme, int indicatorColor)
    {
        mListener = listener;

        // Create a new instance of SlideDateTimeDialogFragment
        SlideDateTimeDialogFragment dialogFragment = new SlideDateTimeDialogFragment();

        // Store the arguments and attach the bundle to the fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("initialDate", initialDate);
        bundle.putSerializable("minDate", minDate);
        bundle.putSerializable("maxDate", maxDate);
        bundle.putBoolean("isClientSpecified24HourTime", isClientSpecified24HourTime);
        bundle.putBoolean("is24HourTime", is24HourTime);
        bundle.putSerializable("defaultDateSelector", defaultDateSelctor);
        bundle.putInt("theme", theme);
        bundle.putInt("indicatorColor", indicatorColor);
        dialogFragment.setArguments(bundle);

        // Return the fragment with its bundle
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        unpackBundle();

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(mInitialDate);

        switch (mTheme)
        {
        case HOLO_DARK:
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
            break;
        case HOLO_LIGHT:
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
            break;
        default:  // if no theme was specified, default to holo light
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.slide_date_time_picker, container);

        setupViews(view);
        customizeViews();
        initViewPager();
        initTabs();
        initButtons();

        return view;
    }

    @Override
    public void onDestroyView()
    {
        // Workaround for a bug in the compatibility library where calling
        // setRetainInstance(true) does not retain the instance across
        // orientation changes.
        if (getDialog() != null && getRetainInstance())
        {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    private void unpackBundle()
    {
        Bundle args = getArguments();

        mInitialDate = (Date) args.getSerializable("initialDate");
        mMinDate = (Date) args.getSerializable("minDate");
        mMaxDate = (Date) args.getSerializable("maxDate");
        mIsClientSpecified24HourTime = args.getBoolean("isClientSpecified24HourTime");
        mIs24HourTime = args.getBoolean("is24HourTime");
        mDefaultDateSelector = (DefaultSelector) args.getSerializable("defaultDateSelector");
        mTheme = args.getInt("theme");
        mIndicatorColor = args.getInt("indicatorColor");
    }

    private void setupViews(View v)
    {
        mViewPager = (CustomViewPager) v.findViewById(R.id.viewPager);
        mSlidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.slidingTabLayout);
        mButtonHorizontalDivider = v.findViewById(R.id.buttonHorizontalDivider);
        mButtonVerticalDivider = v.findViewById(R.id.buttonVerticalDivider);
        mOkButton = (Button) v.findViewById(R.id.okButton);
        mCancelButton = (Button) v.findViewById(R.id.cancelButton);
    }

    private void customizeViews()
    {
        int lineColor = mTheme == HOLO_DARK ?
                getResources().getColor(R.color.gray_holo_dark) :
                getResources().getColor(R.color.gray_holo_light);

        // Set the colors of the horizontal and vertical lines for the
        // bottom buttons depending on the theme.
        switch (mTheme)
        {
        case HOLO_LIGHT:
        case HOLO_DARK:
            mButtonHorizontalDivider.setBackgroundColor(lineColor);
            mButtonVerticalDivider.setBackgroundColor(lineColor);
            break;

        default:  // if no theme was specified, default to holo light
            mButtonHorizontalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
            mButtonVerticalDivider.setBackgroundColor(getResources().getColor(R.color.gray_holo_light));
        }

        // Set the color of the selected tab underline if one was specified.
        if (mIndicatorColor != 0)
            mSlidingTabLayout.setSelectedIndicatorColors(mIndicatorColor);
    }

    private void initViewPager()
    {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        // Setting this custom layout for each tab ensures that the tabs will
        // fill all available horizontal space.
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab, R.id.tabText);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private void initTabs()
    {
        // Set intial date on date tab
        updateDateTab();

        // Set initial time on time tab
        updateTimeTab();

        //Set initial tab
        updateViewPager();
    }

    private void updateViewPager() {
        mViewPager.setCurrentItem(mDefaultDateSelector == DefaultSelector.DATE?0:1);
    }

    private void initButtons()
    {
        mOkButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if (mListener == null)
                {
                    throw new NullPointerException(
                            "Listener no longer exists for mOkButton");
                }

                mListener.onDateTimeSet(new Date(mCalendar.getTimeInMillis()));

                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if (mListener == null)
                {
                    throw new NullPointerException(
                            "Listener no longer exists for mCancelButton");
                }

                mListener.onDateTimeCancel();

                dismiss();
            }
        });
    }

    /**
     * <p>The callback used by the DatePicker to update {@code mCalendar} as
     * the user changes the date. Each time this is called, we also update
     * the text on the date tab to reflect the date the user has currenly
     * selected.</p>
     *
     * <p>Implements the {@link DateFragment.DateChangedListener}
     * interface.</p>
     */
    @Override
    public void onDateChanged(int year, int month, int day)
    {
        mCalendar.set(year, month, day);

        updateDateTab();
    }

    /**
     * <p>The callback used by the TimePicker to update {@code mCalendar} as
     * the user changes the time. Each time this is called, we also update
     * the text on the time tab to reflect the time the user has currenly
     * selected.</p>
     *
     * <p>Implements the {@link TimeFragment.TimeChangedListener}
     * interface.</p>
     */
    @Override
    public void onTimeChanged(int hour, int minute)
    {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);

        updateTimeTab();
    }

    private void updateDateTab()
    {
        mSlidingTabLayout.setTabText(0, DateUtils.formatDateTime(
                mContext, mCalendar.getTimeInMillis(), mDateFlags));
    }

    @SuppressLint("SimpleDateFormat")
    private void updateTimeTab()
    {
        if (mIsClientSpecified24HourTime)
        {
            SimpleDateFormat formatter;

            if (mIs24HourTime)
            {
                formatter = new SimpleDateFormat("HH:mm");
                mSlidingTabLayout.setTabText(1, formatter.format(mCalendar.getTime()));
            }
            else
            {
                formatter = new SimpleDateFormat("h:mm aa");
                mSlidingTabLayout.setTabText(1, formatter.format(mCalendar.getTime()));
            }
        }
        else  // display time using the device's default 12/24 hour format preference
        {
            mSlidingTabLayout.setTabText(1, DateFormat.getTimeFormat(
                    mContext).format(mCalendar.getTimeInMillis()));
        }
    }

    /**
     * <p>Called when the user clicks outside the dialog or presses the <b>Back</b>
     * button.</p>
     *
     * <p><b>Note:</b> Actual <b>Cancel</b> button clicks are handled by {@code mCancelButton}'s
     * event handler.</p>
     */
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);

        if (mListener == null)
        {
            throw new NullPointerException(
                    "Listener no longer exists in onCancel()");
        }

        mListener.onDateTimeCancel();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter
    {
        public ViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
            case 0:
                //dateFragment.setTargetFragment(SlideDateTimeDialogFragment.this, 100);
                return DateFragment.newInstance(
                    mTheme,
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH),
                    mMinDate,
                    mMaxDate);
            case 1:
                //timeFragment.setTargetFragment(SlideDateTimeDialogFragment.this, 200);
                return TimeFragment.newInstance(
                    mTheme,
                    mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE),
                    mIsClientSpecified24HourTime,
                    mIs24HourTime);
            default:
                return null;
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }
}
