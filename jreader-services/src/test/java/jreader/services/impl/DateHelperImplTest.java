package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.TimeZone;

import org.testng.annotations.Test;

import jreader.services.DateHelper;

public class DateHelperImplTest {

    private static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;

    private DateHelper sut = new DateHelperImpl();

    @Test
    public void getCurrentDate_ShouldBeInRange() {
        final long start = System.currentTimeMillis();

        final long actual = sut.getCurrentDate();

        final long end = System.currentTimeMillis();
        assertThat(start <= actual).isTrue();
        assertThat(actual <= end).isTrue();
    }

    @Test
    public void addDaysToCurrentDate_ShouldReturnResultInRange() {
        final int days = 5;
        final long start = System.currentTimeMillis() + days * MILLISECS_PER_DAY;

        final long actual = sut.addDaysToCurrentDate(days);

        final long end = System.currentTimeMillis() + days * MILLISECS_PER_DAY;
        assertThat(start <= actual).isTrue();
        assertThat(actual <= end).isTrue();
    }

    @Test
    public void substractDaysFromCurrentDate_ShouldReturnResultInRange() {
        final int days = 5;
        final long start = System.currentTimeMillis() - days * MILLISECS_PER_DAY;

        final long actual = sut.substractDaysFromCurrentDate(days);

        final long end = System.currentTimeMillis() - days * MILLISECS_PER_DAY;
        assertThat(start <= actual).isTrue();
        assertThat(actual <= end).isTrue();
    }

    @Test
    public void getFirstSecondOfDay_ShouldReturnCorrectResult() {
        final TimeZone timeZone = TimeZone.getTimeZone("GMT");
        final Calendar input = Calendar.getInstance(timeZone);
        input.set(Calendar.YEAR, 2015);
        input.set(Calendar.MONTH, 9);
        input.set(Calendar.DAY_OF_MONTH, 16);
        input.set(Calendar.HOUR_OF_DAY, 16);
        input.set(Calendar.MINUTE, 20);
        input.set(Calendar.SECOND, 55);
        input.set(Calendar.MILLISECOND, 624);
        final Calendar expected = Calendar.getInstance(timeZone);
        expected.clear();
        expected.setTimeZone(timeZone);
        expected.set(Calendar.YEAR, 2015);
        expected.set(Calendar.MONTH, 9);
        expected.set(Calendar.DAY_OF_MONTH, 16);

        final long actual = sut.getFirstSecondOfDay(input.getTimeInMillis());

        assertThat(actual).isEqualTo(expected.getTimeInMillis());
    }

}
