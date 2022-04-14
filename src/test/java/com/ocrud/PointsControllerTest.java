package com.ocrud;

import cn.hutool.core.util.RandomUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class PointsControllerTest  extends  ApplicationTests{
    // 初始化 2k用户 总计2W条积分记录
    @Test
    public void addPoints() throws Exception {
        for (int i = 1; i <= 2000; i++) {
            for (int j = 0; j < 10; j++) {
                super.mockMvc.perform(MockMvcRequestBuilders.get("/points/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userId", i + "")
                        .param("points", RandomUtil.randomNumbers(2))
                        .param("types", "0")
                ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            }
        }
    }
}
