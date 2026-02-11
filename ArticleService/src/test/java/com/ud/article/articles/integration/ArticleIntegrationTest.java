package com.ud.article.articles.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.dto.TagDTO;
import com.ud.article.articles.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ArticleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setup() {
        articleRepository.deleteAll();
        articleRepository.flush();
    }

    @Test
    @DisplayName("Should create article successfully and retrieve the article data by id")
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

    @Test
    @DisplayName("Should create article successfully and delete the article by id")
    void createArticleSuccessfullyAndDeleteTheArticle() throws Exception {
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(1L)
                .title("test title 2")
                .content("test content 2")
                .tags(List.of(TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 2-1")
                                .build(),
                        TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 2-2")
                                .build()
                ))
                .build();

        // save article to database
        mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(articleDTO.getId())); // should return id=1 in this case.

        // delete article by id
        mockMvc.perform(delete("/api/v1/article/{id}", articleDTO.getId()))
                .andExpect(status().isOk());

        // get saved article by id should return an empty Article object with null values
        mockMvc.perform(get("/api/v1/article/{id}", articleDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.title").isEmpty())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.tags").isEmpty());
    }
}
