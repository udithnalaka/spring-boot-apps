package com.ud.article.articles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.dto.TagDTO;
import com.ud.article.articles.service.ArticleServiceImpl;
import com.ud.article.articles.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleServiceImpl articleServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private ArticleDTO articleDTO;

    @BeforeEach
    void setUp() {
        articleDTO = new ArticleDTO();
        articleDTO.setId(1L);
        articleDTO.setTitle("Test Article");
        articleDTO.setContent("Test Content");
        articleDTO.setTags(List.of(TagDTO.builder().articleId(1L).tag("Test tag 1").build()));
    }

    @Test
    void getArticleByIdWhenValidId() throws Exception {
        // Given
        Long articleId = 1L;
        when(articleServiceImpl.getArticleById(articleId)).thenReturn(articleDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/article/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Article"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0].tag").value("Test tag 1"))
                .andDo(print());

        verify(articleServiceImpl).getArticleById(articleId);
    }

    @Test
    void getArticleListByTitleWithValidTitle() throws Exception {
        //Given
        String title = "Test Article";
        when(articleServiceImpl.getArticleByTitle(title)).thenReturn(List.of(articleDTO, articleDTO));

        //When & Then
        mockMvc.perform(get("/api/v1/article/title/{title}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value(title))
                .andDo(print());

        verify(articleServiceImpl).getArticleByTitle(title);
    }

    @Test
    void saveArticleWithValidData() throws Exception {

        //Given
        when(articleServiceImpl.saveArticle(any(ArticleDTO.class))).thenReturn(1L);

        //When & Then
        mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(1));

        verify(articleServiceImpl).saveArticle(articleDTO);
    }

    @Test
    void saveArticleWithInvalidData() throws Exception {

        when(articleServiceImpl.saveArticle(any(ArticleDTO.class))).thenThrow(new RuntimeException("Article contains forbidden words."));

        mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isBadRequest());

        verify(articleServiceImpl).saveArticle(articleDTO);
    }


    @Test
    void deleteArticleByIdWhenValidId() throws Exception {
        Long articleId = 1L;
        doNothing().when(articleServiceImpl).deleteArticle(articleId);


        mockMvc.perform(delete("/api/v1/article/{id}", articleId))
                .andExpect(status().isOk());

        verify(articleServiceImpl).deleteArticle(articleId);
    }

    @Test
    void deleteArticleWithInvalidIdThrowsException() throws Exception {
        Long articleId = 10L;
        doThrow(new ArticleNotFoundException("Article Not found")).when(articleServiceImpl).deleteArticle(articleId);

        mockMvc.perform(delete("/api/v1/article/{id}", articleId))
                .andExpect(status().isBadRequest()).andDo(print());

        verify(articleServiceImpl).deleteArticle(articleId);
    }

}
