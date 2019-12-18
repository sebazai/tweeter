/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;
/**
 *
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessagesTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Before
    public void setup() throws Exception {
        mockMvc.perform(post("/register")
            .param("username", "test")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "tester"))
        .andReturn();
    }
    
    @Test
    @WithMockUser(username = "test", password="tester123", roles="USER")
    public void loggedInUserCanPost() throws Exception {
        mockMvc.perform(post("/shout").param("tweeter", "hi there")).andReturn();
        assertTrue(messagesRep.findAll().get(0).getMessage().contains("hi there"));
    }
}
