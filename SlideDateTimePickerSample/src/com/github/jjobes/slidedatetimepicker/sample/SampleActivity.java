package com.github.jjobes.slidedatetimepicker.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

/**
 * Sample test class for SlideDateTimePicker.
 *
 * @author jjobes
 *
 */
@SuppressLint("SimpleDateFormat")
public class SampleActivity extends ActionBarActivity
{
    private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    private Button mButton;

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

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

        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                    .setListener(listener)
                    .setInitialDate(new Date())
                    //.setMinDate(minDate)
                    //.setMaxDate(maxDate)
                    //.setIs24HourTime(true)
                    .setTheme(SlideDateTimePicker.HOLO_LIGHT)
                    .setIndicatorColor(Color.parseColor("#608393"))
                    .build()
                    .show();
            }
        });
    }
}
