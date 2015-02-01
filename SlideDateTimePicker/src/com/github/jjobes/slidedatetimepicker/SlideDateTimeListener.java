package com.github.jjobes.slidedatetimepicker;

import java.io.Serializable;
import java.util.Date;

/**
 * This listener class informs the client when the user sets
 * a date and time by pressing "OK" or cancels the dialog.
 *
 * Overriding onDateTimeCancel() is optional. The client can
 * always override this to listen for when the user cancels
 * the dialog. This is called when the user presses the Cancel
 * button, touches outside the dialog or presses the Back button.
 *
 * @author jjobes
 *
 */
public abstract class SlideDateTimeListener implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Informs the client when the user presses "OK"
     * and selects a date and time.
     *
     * @param date  The Date object that contains the date
     *              and time that the user has selected.
     */
    public abstract void onDateTimeSet(Date date);

    /**
     * Informs the client when the user cancels the
     * dialog by pressing Cancel, touching outside
     * the dialog or pressing the Back button.
     * This override is optional.
     */
    public void onDateTimeCancel()
    {

    }
}
