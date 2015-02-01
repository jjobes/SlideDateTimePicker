Contributions Requested
=======================
There is a strange problem that needs to be resolved regarding touch event handling. When the dialog is displayed and the TimePicker is chosen, then you rotate the device, when you spin the TimePicker spinners, those events are getting routed to the DatePicker and vice versa. Please see [CustomViewPager](https://github.com/jjobes/SlideDateTimePicker/blob/master/SlideDateTimePicker/src/com/github/jjobes/slidedatetimepicker/CustomViewPager.java) 's `dispatchTouchEvent()` for the problem code. `dispatchTouchEvent()` is where I am trying to allow the ViewPager to swipe horizontally, while at the same time allowing the Date or TimePicker to swipe vertically. I believe the correct solutions may involve not overriding `dispatchTouchEvent()`, but instead overriding `onInterceptTouchEvent()` and `onTouchEvent()`, but I haven't been able to get that to work either.

SlideDateTimePicker
===================

SlideDateTimePicker is an Android library that displays a single DialogFragment in which the user can select a date and a time. The user can swipe between the DatePicker and TimePicker, and the tab underline will gradually animate as the user swipes. The colors of the tab indicator and divider lines are customizable to fit your project's theme. Tested on Android 4.0+.

<img src="https://raw.github.com/jjobes/SlideDateTimePicker/master/screenshots/1.png" width="270" style="margin-right:10px;">
<img src="https://raw.github.com/jjobes/SlideDateTimePicker/master/screenshots/2.png" width="270">

Setup
=====

**Eclipse/ADT**:
From your main project, simply reference the SlideDateTimePicker library:

Right click on your project name and select Properties.

Select Android from the left column.

Click Add.

Select SlideDateTimePicker.

Click Apply and then OK.

**Android Studio**:
Coming soon.

How to Use
==========
(See [SampleActivity](https://github.com/jjobes/SlideDateTimePicker/blob/master/SlideDateTimePickerSample/src/com/github/jjobes/slidedatetimepicker/sample/SampleActivity.java) for a more complete example)

First create a listener object:

``` java
private SlideDateTimeListener listener = new SlideDateTimeListener() {

    @Override
    public void onDateTimeSet(Date date)
    {
        // Do something with the date. This Date object contains
        // the date and time that the user has selected.
    }

    @Override
    public void onDateTimeCancel()
    {
        // Overriding onDateTimeCancel() is optional.
    }
};
```

Then pass the listener into the builder and show the dialog:

``` java
new SlideDateTimePicker.Builder(getSupportFragmentManager())
    .listener(listener)
    .initialDate(new Date())
    .build()
    .show();
```

Note that the `Date` object that you pass in to `.initialDate()` should contain both the date and time that you wish to initially display.

**To set the minimum date to display:**

``` java
.minDate(date)
```

**To set the maximum date to display:**
``` java
.maxDate(date)
```

The default time format is the current device's default, but you can force a 24-hour or 12-hour time format:

**To force 24-hour time:**

``` java
.is24HourTime(true)
```

**To force 12-hour time:**
``` java
.is24HourTime(false)
```

**The default theme is Holo Light, but you can specify either Holo Light or Dark explicitly:**
``` java
.theme(SlideDateTimePicker.HOLO_LIGHT)
```
or
``` java
.theme(SlideDateTimePicker.HOLO_DARK)
```

**To specify the color for the sliding tab underline (indicator):**
``` java
.indicatorColor(Color.parseColor("#FF0000"))
```

**To specify the color of the horizontal divider lines in the DatePicker and TimePicker:**
You can also set a custom color for the horizontal divider lines in the DatePicker and TimePicker, but for this you have to paste your own version of selection_divider.9.png into the the library's drawable-xxxx folders that has your desired color. To do this, open selection_divider.9.png in a graphics editor, change the color, then paste your new files into the drawable-xxxx folders.

Contributing
============
Contributions are welcome! Please open up an issue in GitHub or submit a PR.

License
=======
Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Please see [LICENSE](https://github.com/jjobes/SlideDateTimePicker/blob/master/LICENSE)

Acknowledgements
================
Thanks to Arman Pagilagan's [blog post](http://armanpagilagan.blogspot.com/2014/05/creating-custom-date-and-time-picker-in.html) for the initial idea.