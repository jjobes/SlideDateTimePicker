package com.github.jjobes.slidedatetimepicker.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sample test class for SlideDateTimePicker.
 *
 * @author jjobes
 *
 */
@SuppressLint("SimpleDateFormat")
public class SampleActivity extends FragmentActivity
{
    private final SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");

    private final SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            Toast.makeText(SampleActivity.this,
                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
            Toast.makeText(SampleActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        Button mButton = findViewById(R.id.button);

        mButton.setOnClickListener(v -> new SlideDateTimePicker.Builder(getSupportFragmentManager())
            .setListener(listener)
            .setInitialDate(new Date())
            //.setMinDate(minDate)
            //.setMaxDate(maxDate)
            //.setIs24HourTime(true)
            //.setTheme(SlideDateTimePicker.HOLO_DARK)
            //.setIndicatorColor(Color.parseColor("#990000"))
            .build()
            .show());
    }
}
