package com.example.is_coursework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class OpenApiGenerateTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String PATH_TO_SAVE_JSON_FILE = "./open-api.json";
    private static final String ROUTE_TO_API_DOCS = "/v3/api-docs";

    @Test
    void saveApiDocs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROUTE_TO_API_DOCS))
                .andDo(result -> Files.write(new File(PATH_TO_SAVE_JSON_FILE).toPath(),
                        result.getResponse().getContentAsString(StandardCharsets.UTF_8).getBytes()));
    }

}
