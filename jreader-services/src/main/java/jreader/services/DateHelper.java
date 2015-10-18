package jreader.services;

public interface DateHelper {
    
    long getCurrentDate();
    
    long addDaysToCurrentDate(int days);
    
    long substractDaysFromCurrentDate(int days);
    
    long getFirstSecondOfDay(long date);

}
