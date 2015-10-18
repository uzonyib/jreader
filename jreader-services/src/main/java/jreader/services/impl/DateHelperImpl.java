package jreader.services.impl;

import java.util.Calendar;
import java.util.TimeZone;

import jreader.services.DateHelper;

public class DateHelperImpl implements DateHelper {
    
    private static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;
    
    @Override
    public long getCurrentDate() {
        return System.currentTimeMillis();
    }
    
    @Override
    public long addDaysToCurrentDate(final int days) {
        return getCurrentDate() + MILLISECS_PER_DAY * (long) days;
    }
    
    @Override
    public long substractDaysFromCurrentDate(final int days) {
        return addDaysToCurrentDate(-days);
    }
    
    @Override
    public long getFirstSecondOfDay(final long date) {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

}
