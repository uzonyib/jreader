package jreader.dto;

import java.util.Calendar;
import java.util.Date;

import lombok.Value;

@Value
public class UserDto {

    private String username;
    private String role;
    private Long registrationDate;
    
    public Date getRegistrationDateAsDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(registrationDate);
        return calendar.getTime();
    }

}
