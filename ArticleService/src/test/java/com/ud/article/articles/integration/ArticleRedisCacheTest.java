package com.ud.article.articles.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.dto.TagDTO;
import com.ud.article.articles.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class ArticleRedisCacheTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>("redis:8.4.0")
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ArticleRepository articleRepository;

    @MockitoSpyBean
    private ArticleRepository articleRepositorySpy;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        articleRepository.deleteAll();
    }

    @Test
    void testCreateArticleAndCacheIt_ThenShouldBeAvailableInCache() throws Exception{
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .tags(List.of(TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 1")
                                .build(),
                        TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 2")
                                .build()
                ))
                .build();

        // step 1. save Article to database
        MvcResult savedResult = mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArticleDTO savedArticle = objectMapper.readValue(savedResult.getResponse().getContentAsString(), ArticleDTO.class);
        Long articleId = savedArticle.getId();

        // step 2. check Article exists in database
        assertTrue(articleRepository.findById(articleId).isPresent());

        // step 3. check cache
        Cache cache = cacheManager.getCache("ARTICLE_CACHE");
        assertNotNull(cache);
        assertNotNull(cache.get(articleId.toString(), ArticleDTO.class));
    }

    @Test
    void testGetArticleAndVerifyInCache() throws Exception{
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .tags(List.of(TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 1")
                                .build(),
                        TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 2")
                                .build()
                ))
                .build();

        // step 1. save Article to database
        MvcResult savedResult = mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArticleDTO savedArticle = objectMapper.readValue(savedResult.getResponse().getContentAsString(), ArticleDTO.class);
        Long articleId = savedArticle.getId();

        // step 2. check Article exists in database
        mockMvc.perform(get("/api/v1/article/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(articleDTO.getId()));

        Mockito.verify(articleRepositorySpy, Mockito.times(1)).findById(articleId);

        // step 3. check cache
        Cache cache = cacheManager.getCache("ARTICLE_CACHE");
        assertNotNull(cache);
        assertNotNull(cache.get(articleId.toString(), ArticleDTO.class));
    }

    @Test
    void testDeleteArticleShouldEvictArticleInCache() throws Exception{
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .tags(List.of(TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 1")
                                .build(),
                        TagDTO.builder()
                                .articleId(1L)
                                .tag("test tag 2")
                                .build()
                ))
                .build();

        // step 1. save Article to database
        MvcResult savedResult = mockMvc.perform(post("/api/v1/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArticleDTO savedArticle = objectMapper.readValue(savedResult.getResponse().getContentAsString(), ArticleDTO.class);
        Long articleId = savedArticle.getId();

        // step 2. delete Article
        mockMvc.perform(delete("/api/v1/article/{id}", articleId))
                .andExpect(status().isOk());

        // step 3. check Article deleted from database
        assertFalse(articleRepository.findById(articleId).isPresent());

        // step 4. check cache evict
        Cache cache = cacheManager.getCache("ARTICLE_CACHE");
        assertNotNull(cache);
        assertNull(cache.get(articleId));

    }
}
