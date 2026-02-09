package com.ud.article.ArticleService.service;

import com.ud.article.ArticleService.dto.ArticleDTO;
import com.ud.article.ArticleService.dto.TagDTO;
import com.ud.article.ArticleService.model.Article;
import com.ud.article.ArticleService.model.Tag;
import com.ud.article.ArticleService.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Spy
    private ModelMapper mapper;

    @Captor
    private ArgumentCaptor<Article> articleCaptor;

    private Article sampleArticle;

    private String blackListedWords;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    public void setUp() {
        // Optional: Can use MockitoAnnotations.openMocks(this); if not using @ExtendWith(MockitoExtension.class)
        sampleArticle = Article.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .tags(List.of(Tag.builder()
                                .articleId(1L)
                                .tag("test tag")
                                .build(),
                        Tag.builder()
                                .articleId(2L)
                                .tag("test tag 2")
                                .build()
                ))
                .build();
    }

    @Test
    public void shouldReturnArticleWhenArticleExists() {

        when(articleRepository.findById(1L)).thenReturn(Optional.of(sampleArticle));

        ArticleDTO articleDto = articleService.findById(1L);

        // Assert
        assertNotNull(articleDto);
        assertEquals(sampleArticle.getTitle(), articleDto.getTitle());

        // Verify that the findById method was called on the mock repository
        verify(articleRepository).findById(1L);
    }

    @Test
    void shouldReturnArticleWhenArticleExistsByTitle() {

        String title = "test title";

        when(articleRepository.findByTitle(title)).thenReturn(List.of(sampleArticle, sampleArticle));

        List<ArticleDTO> articlesDto = articleService.findByTitle(title);

        assertFalse(articlesDto.isEmpty());
        assertEquals(2, articlesDto.size());
        assertEquals(sampleArticle.getTitle(), articlesDto.getFirst().getTitle());

        verify(articleRepository).findByTitle(title);
    }

    @Test
    void shouldReturnNewArticleIdWhenSavingArticle() {

        ReflectionTestUtils.setField(articleService, "blackListedWords", "banned_word1,banned_word2");

        ArticleDTO articleDto = ArticleDTO.builder()
                .title("test title 3")
                .content("test content 3")
                .tags(List.of(TagDTO.builder()
                        .articleId(3L)
                        .tag("test tag 3")
                        .build()))
                .build();

        Article newArticle = Article.builder()
                .title("test title 3")
                .content("test content 3")
                .tags(List.of(Tag.builder()
                        .articleId(3L)
                        .tag("test tag 3")
                        .build()
                ))
                .build();

        Article savedArticle = Article.builder()
                .id(3L)
                .title("test title 3")
                .content("test content 3")
                .tags(List.of(Tag.builder()
                        .id(3L)
                        .articleId(3L)
                        .tag("test tag 3")
                        .build()
                ))
                .build();

        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        Long newArticleId = articleService.create(articleDto);

        assertEquals(3L, newArticleId);

        verify(articleRepository).save(newArticle);

    }

    @Test
    void shouldUpdateArticleWithValidArticleId() {

        //Given
        Long articleId = 3L;
        ReflectionTestUtils.setField(articleService, "blackListedWords", "banned_word1,banned_word2");

        ArticleDTO articleDto = ArticleDTO.builder()
                .title("test title 3")
                .content("test content 3")
                .tags(List.of(TagDTO.builder()
                        .articleId(3L)
                        .tag("test tag 3 updated")
                        .build()))
                .build();

        Article savedArticle = Article.builder()
                .id(3L)
                .title("test title 3")
                .content("test content 3")
                .tags(List.of(Tag.builder()
                        .id(3L)
                        .articleId(3L)
                        .tag("test tag 3")
                        .build()
                ))
                .build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(savedArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

        //When
        Long updatedArticleId = articleService.update(articleId, articleDto);

        // Then - Capture the argument passed to save()
        verify(articleRepository).save(articleCaptor.capture());

        Article updatedArticle = articleCaptor.getValue();
        assertEquals(articleId, updatedArticle.getId());
        assertEquals(articleDto.getTitle(), updatedArticle.getTitle());
        assertEquals(articleDto.getContent(), updatedArticle.getContent());
        assertFalse(updatedArticle.getTags().isEmpty());
        assertEquals(1, updatedArticle.getTags().size());
        assertEquals("test tag 3 updated", updatedArticle.getTags().getFirst().getTag());

    }

    @Test
    void shouldDeleteArticleWithValidArticleId() {

        //Given
        Long articleId = 1L;

        //When
        articleService.delete(articleId);

        //Then
        verify(articleRepository).deleteById(articleId);
        //OR
        verify(articleRepository, times(1)).deleteById(articleId);
    }
}
