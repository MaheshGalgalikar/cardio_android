package md.fusionworks.android.cardio.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by admin on 24.08.2015.
 */
public class DateUtils {

    private static SimpleDateFormat historicalDataDateFormat;
    private static SimpleDateFormat historicalDataTimeFormat;

    public static Calendar addNMinutesToCalendar(Calendar calendar, int minutes) {

        calendar.add(Calendar.MINUTE, minutes);

        return calendar;
    }

    public static SimpleDateFormat getHistoricalDataDateFormat() {
        if (historicalDataDateFormat == null) {
            historicalDataDateFormat = new SimpleDateFormat("MMMM d, yyyy   h:mma", Locale.US);
        }
        return historicalDataDateFormat;
    }

    public static SimpleDateFormat getHistoricalDataTimeFormat() {
        if (historicalDataTimeFormat == null) {
            historicalDataTimeFormat = new SimpleDateFormat("h:mma", Locale.US);
        }
        return historicalDataTimeFormat;
    }
}
