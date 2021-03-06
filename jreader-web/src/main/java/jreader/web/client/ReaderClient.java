package jreader.web.client;

public interface ReaderClient {

    void createGroup(String title);

    void deleteGroup(Long groupId);

    void renameGroup(Long groupId, String title);

    void moveGroupUp(Long groupId);

    void moveGroupDown(Long groupId);

}
