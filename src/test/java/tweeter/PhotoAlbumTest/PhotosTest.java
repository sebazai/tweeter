/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.PhotoAlbumTest;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;
/**
 *
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class PhotosTest {
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;

    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Before
    public void setup() throws Exception {
        messagesRep.deleteAllInBatch();
        accountRep.deleteAllInBatch();
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(SecurityMockMvcConfigurers.springSecurity())
          .build();
        mockMvc.perform(post("/register")
            .param("username", "test")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "tester"))
        .andReturn();
    }
    
    @Test
    @WithMockUser("test")
    public void photoAlbumPageShown() throws Exception {
        MvcResult res = mockMvc.perform(get("/users/tester/album")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Insert new photos"));
    }
}
