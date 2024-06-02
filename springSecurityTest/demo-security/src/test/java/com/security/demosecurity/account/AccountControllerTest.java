package com.security.demosecurity.account;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    @WithAnonymousUser
    void testCreateAccount() throws Exception{
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    void index_user() throws Exception{
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "keesun", roles = "ADMIN")
    void index_admin() throws Exception{
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    void admin_user() throws Exception{
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "keesun", roles = "ADMIN")
    void admin_amin() throws Exception{
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void login_success() throws Exception{
        String keesun = "keesun";
        String password = "123";
        Account user = this.createUser(keesun, password);
        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void login_success2() throws Exception{
        String keesun = "keesun";
        String password = "123";
        Account user = this.createUser(keesun, password);
        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());
    }

    @Test
    public void login_fail() throws Exception{
        String keesun = "keesun";
        String password = "123";
        Account user = this.createUser(keesun, password);
        mockMvc.perform(formLogin().user(user.getUsername()).password("password"))
                .andExpect(unauthenticated());
    }

    private Account createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");

        accountService.createNew(account);

        return account;
    }


}
