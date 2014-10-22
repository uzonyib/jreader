package jreader.test.acceptance.step;

final class Constants {
    
    public static final String BASE_URL = "http://localhost:8080/";
    public static final String MAIN_PAGE_URL = BASE_URL + "reader";
    public static final String LOGIN_PAGE_URL = BASE_URL + "_ah/login?continue=" + MAIN_PAGE_URL;
    public static final String TITLE = "jReader";
    
    private Constants() {
        
    }

}
