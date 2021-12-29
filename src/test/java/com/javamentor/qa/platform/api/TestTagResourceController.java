package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestTagResourceController extends AbstractClassForDRRiderMockMVCTests {

    public String getTokens(String email) throws Exception {
        String tokenJS = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\" : \"" + email + "\"," +
                        " \"password\" : \"password\"}")
        ).andReturn().getResponse().getContentAsString();
        return new JSONObject(tokenJS).getString("token");
    }

    @Test
    @DataSet(value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/emptyTrackedTag.yml",
            "dataset/testTagResourceController/emptyTag.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifEmptyTrackedTag() throws Exception {
        mockMvc.perform(get("http://localhost:8091/api/user/tag/tracked")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DataSet(value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/trackedTag2.yml",
            "dataset/testTagResourceController/tag2.yml",
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifHasTwoTags() throws Exception {
        mockMvc.perform(get("http://localhost:8091/api/user/tag/tracked")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].persistDateTime").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].persistDateTime").exists());
    }

    @Test
    @DataSet(value = {
            "dataset/testTagResourceController/roles.yml",
            "dataset/testTagResourceController/users.yml",
            "dataset/testTagResourceController/trackedTag3.yml",
            "dataset/testTagResourceController/tag3.yml"
    },
            strategy = SeedStrategy.CLEAN_INSERT,
            cleanAfter = true)
    public void ifHasTwoTagsAndOneOther() throws Exception {
        mockMvc.perform(get("http://localhost:8091/api/user/tag/tracked")
                .header("Authorization", "Bearer " + getTokens("user100@mail.ru")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(102))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].persistDateTime").exists())
                .andExpect(jsonPath("$[1].id").value(103))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].persistDateTime").exists());
    }
}
