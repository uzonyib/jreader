package jreader.web.controller.ajax;

import static org.mockito.Mockito.when;

import java.security.Principal;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public int groupCountOf(String username) throws Exception {
        userService.ensureIsRegistered(username);
        when(principal.getName()).thenReturn(username);

        return groupController.listAll(principal).size();
    }

}
