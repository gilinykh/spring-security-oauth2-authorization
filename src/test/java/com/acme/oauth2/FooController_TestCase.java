package com.acme.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.util.Base64;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ServiceLauncher.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class FooController_TestCase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() throws Exception {
        String basic = new String(Base64.getEncoder().encode("clientIdPassword:secret".getBytes()));
        MvcResult result = mockMvc.perform(post("/oauth/token")
                .header("Authorization", "Basic " + basic)
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .param("grant_type", "password")
                .param("username", "john")
                .param("password", "123")
                .param("client_id", "clientIdPassword"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String,String> json = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String,String>>(){});

        System.out.println("Access token: " + json.get("access_token"));
        mockMvc.perform(get("/foos/{fooId}", 1)
                .cookie(new Cookie("access_token", json.get("access_token")))
                .header("Authorization", "Bearer " + json.get("access_token")))
                .andExpect(status().isOk());
    }
}
