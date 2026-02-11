package com.ud.article.articles.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.dto.TagDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class ArticleIntegrationTestWithPostgres {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create article successfully and retrieve the article data by id in PostgresDB")
    void createArticleSuccessfullyAndRetrieveTheArticle() throws Exception {
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .tags(List.of(TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 1")
                                .build(),
                        TagDTO.builder()
                                .articleId(2L)
                                .tag("test tag 2")
                                .build()
                ))
                .build();

        // save article to database
        mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(articleDTO.getId())); // should return id=1 in this case.


        // get saved article by id
        mockMvc.perform(get("/api/v1/article/{id}", articleDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(articleDTO.getId()))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.content").value("test content"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0].tag").value("test tag 1"))
                .andExpect(jsonPath("$.tags[1].tag").value("test tag 2"));
    }
}
