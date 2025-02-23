package org.noashark.app.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noashark.app.user.pojo.dto.UserDTO;
import org.noashark.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 点播控制器测试
 *
 * @author zhangxt
 * @date 2024/03/24 14:47
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = org.noashark.app.ScaffoldingApplication.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    @Test
    public void addUser() throws Exception {

        String url = "/api/users";

        log.info("url:{}", url);

        UserDTO userDTO = new UserDTO();
        userDTO.setAvatar("test");
        userDTO.setMobile("124555");
        userDTO.setName("allen");
        userDTO.setEmail("allen@qq.com");
        userDTO.setGender((short)1);
        userDTO.setDeptId((long)1);
        userDTO.setNickname("allen");
        userDTO.setStatus((short)1);
        userDTO.setPassword("123456");

        String responseString = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .content(JsonUtils.toJson(userDTO).getBytes())
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        log.info("response:{}", responseString);



    }
}
