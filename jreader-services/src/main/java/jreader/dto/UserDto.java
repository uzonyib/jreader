package jreader.dto;

import java.util.Calendar;
import java.util.Date;

public class UserDto {

    private String username;
    private String role;
    private Long registrationDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public Date getRegistrationDateAsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(registrationDate);
        return calendar.getTime();
    }

}
