/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.ProfileWallTest;
import java.util.List;
import javax.transaction.Transactional;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tweeter.domain.Account;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.CommentsRepository;
import tweeter.repositories.LikesRepository;
import tweeter.repositories.MessagesRepository;
import tweeter.services.AccountService;
import tweeter.services.MessagesService;
/**
 *
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class MessagesTest {
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Autowired
    private CommentsRepository commentsRep;
    
    @Autowired
    private LikesRepository likesRep;

    @Before
    public void setup() throws Exception {
        commentsRep.deleteAllInBatch();
        likesRep.deleteAllInBatch();
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
    
    @WithMockUser(username = "test", password="tester123")
    @Test
    public void loggedInUserCanPost() throws Exception {
        mockMvc.perform(post("/shout").param("tweeter", "hi there")).andReturn();
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("hi there"));
    }
    
    @WithMockUser("test")
    @Test
    public void cantPostTwoCharTweetWhenMinimumIsThree() throws Exception {
        MvcResult res = mockMvc.perform(post("/shout").param("tweeter", "12")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("3-100 chars."));
    }
    
    @WithMockUser("test")
    @Test
    public void cantPost101CharTweetWhenMaxIs100() throws Exception {
        MvcResult res = mockMvc.perform(post("/shout").param("tweeter", "xynlojoqddkmhtresnymqlhhtxjgpfqcpnyjgvbxstezozieaygtoojekogyhtdejdlbkfadubkfaahnhqorhqcwxadnkzophfkkecudpoyulfcbsjwczphzchzhnfhmnerjgazjhxbjalqmtbyrh")).andReturn();
    }
    
    @WithMockUser("test")
    @Test
    public void canLikeOwnPost() throws Exception {
        makePost();
        List<Messages> postToLike = messagesRep.findAll();
        mockMvc.perform(post("/like/tester/" + postToLike.get(0).getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester")).andReturn();
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Likes: 1"));
    }
    
    @WithMockUser("test")
    @Test
    public void cantLikePostTwice() throws Exception {
        makePost();
        List<Messages> postToLike = messagesRep.findAll();
        mockMvc.perform(post("/like/tester/" + postToLike.get(0).getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester")).andReturn();
        mockMvc.perform(post("/like/tester/" + postToLike.get(0).getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester")).andReturn();
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Likes: 1"));
    }
    
    @WithMockUser("test")
    @Test
    public void canCommentOwnPost() throws Exception {
        makePost();
        List<Messages> postToLike = messagesRep.findAll();
        mockMvc.perform(post("/comment/test/" + postToLike.get(0).getId()).param("comment", "well this is nice")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester")).andReturn();
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("well this is nice"));
        assertTrue(content.contains("123"));
    }
    
    @WithMockUser("test")
    @Test
    public void tooShortCommentNotAllowed() throws Exception {
        makePost();
        List<Messages> postToComment = messagesRep.findAll();
        MvcResult res = mockMvc.perform(post("/comment/test/" + postToComment.get(0).getId()).param("comment", "!1")).andExpect(status().isOk()).andReturn();
        assertFalse(res.getResponse().getContentAsString().contains("!1"));
        assertTrue(res.getResponse().getContentAsString().contains("Comment length between 3-100 chars"));
    }
    
    @WithMockUser("test")
    @Test
    public void tooLongCommentNotAllowed() throws Exception {
        makePost();
        List<Messages> postToComment = messagesRep.findAll();
        MvcResult res = mockMvc.perform(post("/comment/test/" + postToComment.get(0).getId())
                .param("comment", "xynlojoqddkmhtresnymqlhhtxjgpfqcpnyjgvbxstezozieaygtoojekogyhtdejdlbkfadubkfaahnhqorhqcwxadnkzophfkkecudpoyulfcbsjwczphzchzhnfhmnerjgazjhxbjalqmtbyrh"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Comment length between 3-100 chars"));
    }
    
    public Account makePost() throws Exception {
        mockMvc.perform(post("/shout").param("tweeter", "123")).andReturn();
        Account m = accountService.findAccount("tester");
        return m;
    }
    
}
