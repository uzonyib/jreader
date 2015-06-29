package jreader.web.controller.ajax;

import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;

import jreader.dto.SubscriptionGroupDto;
import jreader.services.UserService;
import jreader.web.controller.PageController;
import jreader.web.test.AbstractDataStoreTest;
import jreader.web.test.ConcordionSpringJunit4ClassRunner;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(ConcordionSpringJunit4ClassRunner.class)
@ContextConfiguration("classpath:jreader-web.xml")
public class GroupControllerFixture extends AbstractDataStoreTest {

    @Autowired
    private PageController pageController;
    @Autowired
    private GroupController groupController;
    
    @Autowired
    private UserService userService;

    @Mock
    private Principal principal;
    
    private String username;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    public void setUsername(String username) {
        this.username = username;
        userService.ensureIsRegistered(this.username);
        when(principal.getName()).thenReturn(username);
    }

    public int getGroupCount() throws Exception {
        return groupController.listAll(principal).size();
    }
    
    public List<SubscriptionGroupDto> getGroups() throws Exception {
        return groupController.listAll(principal);
    }
    
    public void createGroup(String title) {
        groupController.create(principal, title);
    }
    
    private Long getIdForIndex(int index) {
        return Long.valueOf(groupController.listAll(principal).get(index - 1).getId());
    }
    
    public void deleteGroup(int index) {
        groupController.delete(principal, getIdForIndex(index));
    }
    
    public void entitleGroup(int index, String title) {
        groupController.entitle(principal, getIdForIndex(index), title);
    }
    
    public void moveGroup(int index, String direction) {
        groupController.move(principal, getIdForIndex(index), "up".equals(direction));
    }

}
