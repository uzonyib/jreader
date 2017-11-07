package jreader.web.controller.ajax;

import static org.mockito.BDDMockito.given;

import java.security.Principal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
public abstract class ControllerTest {

    protected static final String USERNAME = "test_user";

    @MockBean
    protected Principal principal;

    @Autowired
    protected MockMvc mvc;

    @Before
    public void setupPrincipal() {
        given(principal.getName()).willReturn(USERNAME);
    }

}
