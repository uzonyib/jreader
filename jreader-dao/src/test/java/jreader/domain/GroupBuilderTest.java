package jreader.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Group.Builder;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GroupBuilderTest extends AbstractDaoTest {
    
    private static final String USERNAME = "username";
    private static final long ID = 10L;
    private static final String TITLE = "title";
    private static final int ORDER = 123;
    
    private User savedUser;
    private Builder builder;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername(USERNAME);
        UserDao userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
        
        this.builder = new Builder();
    }
    
    @Test
    public void build_shouldReturnGroup() {
        Group group = builder.id(ID).title(TITLE).order(ORDER).user(savedUser).build();
        
        assertNotNull(group);
        assertNotNull(group.getId());
        assertEquals(group.getId().longValue(), ID);
        assertEquals(group.getTitle(), TITLE);
        assertEquals(group.getOrder(), ORDER);
        assertNotNull(group.getUser());
        assertEquals(group.getUser().getUsername(), USERNAME);
    }

}
