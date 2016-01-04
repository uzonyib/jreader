package jreader.test.acceptance;

public final class Constants {
    
    public static final String BASE_URL = "http://localhost:8080/";
    public static final String MAIN_PAGE_URL = BASE_URL + "reader";
    public static final String LOGIN_PAGE_URL = BASE_URL + "_ah/login?continue=" + MAIN_PAGE_URL;
    public static final String USER_ADMIN_PAGE_URL = BASE_URL + "admin/users";
    public static final int WAIT_TIMEOUT = 5;
    
    private Constants() {
        
    }

}
