package jreader.services.impl;

import jreader.services.DateHelper;

public class DateHelperImpl implements DateHelper {
    
    private static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;
    
    @Override
    public long addDaysToCurrentDate(final int days) {
        return System.currentTimeMillis() + MILLISECS_PER_DAY * (long) days;
    }

}