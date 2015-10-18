package jreader.services.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.TimeZone;

import org.testng.annotations.Test;

import jreader.services.DateHelper;

public class DateHelperImplTest {
    
    private static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;
    
    private DateHelper sut = new DateHelperImpl();
    
    @Test
    public void getCurrentDate() {
        long start = System.currentTimeMillis();
        long date = sut.getCurrentDate();
        long end = System.currentTimeMillis();
        
        assertTrue(start <= date);
        assertTrue(date <= end);
    }
    
    @Test
    public void addDaysToCurrentDate() {
        final int days = 5;
        long start = System.currentTimeMillis() + days * MILLISECS_PER_DAY;
        long date = sut.addDaysToCurrentDate(days);
        long end = System.currentTimeMillis() + days * MILLISECS_PER_DAY;
        
        assertTrue(start <= date);
        assertTrue(date <= end);
    }
    
    @Test
    public void substractDaysFromCurrentDate() {
        final int days = 5;
        long start = System.currentTimeMillis() - days * MILLISECS_PER_DAY;
        long date = sut.substractDaysFromCurrentDate(days);
        long end = System.currentTimeMillis() - days * MILLISECS_PER_DAY;
        
        assertTrue(start <= date);
        assertTrue(date <= end);
    }
    
    @Test
    public void getFirstSecondOfDay() {
        final TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar input = Calendar.getInstance(timeZone);
        input.set(Calendar.YEAR, 2015);
        input.set(Calendar.MONTH, 9);
        input.set(Calendar.DAY_OF_MONTH, 16);
        input.set(Calendar.HOUR_OF_DAY, 16);
        input.set(Calendar.MINUTE, 20);
        input.set(Calendar.SECOND, 55);
        input.set(Calendar.MILLISECOND, 624);
        Calendar expected = Calendar.getInstance(timeZone);
        expected.clear();
        expected.setTimeZone(timeZone);
        expected.set(Calendar.YEAR, 2015);
        expected.set(Calendar.MONTH, 9);
        expected.set(Calendar.DAY_OF_MONTH, 16);
        
        long date = sut.getFirstSecondOfDay(input.getTimeInMillis());
        
        assertEquals(date, expected.getTimeInMillis());
    }

}
