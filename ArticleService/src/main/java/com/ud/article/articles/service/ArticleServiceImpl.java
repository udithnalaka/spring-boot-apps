package com.ud.article.articles.service;


import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.model.Article;
import com.ud.article.articles.model.Tag;
import com.ud.article.articles.repository.ArticleRepository;
import com.ud.article.articles.exception.ArticleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource("classpath:articles_blacklist.properties")
public class ArticleServiceImpl implements ArticleService {

    //@Value("${app.black.listed.words}")
    private final String blackListedWords;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public ArticleServiceImpl(@Value("${app.black.listed.words}") String blackListedWords,
                              ArticleRepository articleRepository, ModelMapper mapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = mapper;
        this.blackListedWords = blackListedWords;
    }

    public ArticleDTO getArticleById(Long id) {

        log.info("get Article by ID: {}", id);

        try {
            Article article = articleRepository.findById(id)
                    .orElse(new Article());

            return modelMapper.map(article, ArticleDTO.class);

        } catch (Exception e) {
            log.error("Error when retrieving article id: {} from database. ", id, e);
            throw e;
        }
    }

    public List<ArticleDTO> getArticleByTitle(String title) {
        log.info("get Articles by title: {}", title);

        return articleRepository.findByTitle(title)
                .stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Long saveArticle(ArticleDTO articleDTO) {

        //validation check
        checkForbiddenWords(articleDTO);

        try {
            Article newArticle = Article.builder()
                    .title(articleDTO.getTitle())
                    .content(articleDTO.getContent())
                    .tags(articleDTO.getTags() != null ?
                            articleDTO.getTags().stream()
                                    .map(tagDTO -> Tag.builder()
                                            .articleId(tagDTO.getArticleId())
                                            .tag(tagDTO.getTag())
                                            .build()
                                    ).collect(Collectors.toList()) : null)
                    .build();

            Article savedArticle = articleRepository.save(newArticle);
            log.info("Article saved to database. Article ID: {}", savedArticle.getId());
            return savedArticle.getId();

        } catch (Exception e) {
            log.error("Error when saving to database. ", e);
            throw e;
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Long updateArticle(Long id, ArticleDTO articleDTO) throws ArticleNotFoundException {
        //validation check
        checkForbiddenWords(articleDTO);

        log.info("Updating Article ID: {}", id);

        try {
            Article updateArticle = articleRepository.findById(id)
                    .orElseThrow(() -> new ArticleNotFoundException("Article not found"));

            updateArticle.setTitle(articleDTO.getTitle());
            updateArticle.setContent(articleDTO.getContent());

            List<Tag> tagList = articleDTO.getTags().stream()
                    .map(tagDto -> Tag.builder()
                            .articleId(tagDto.getArticleId())
                            .tag(tagDto.getTag())
                            .build()
                    ).collect(Collectors.toList());
           updateArticle.setTags(tagList);

            Article updatedArticle = articleRepository.save(updateArticle);
            log.info("Article updated in database. Article ID: {}", updatedArticle.getId());

            return updatedArticle.getId();

        } catch (Exception e) {
            log.error("Error when updating Article to database. ", e);
            throw e;
        }

    }

    public void deleteArticle(Long id) throws ArticleNotFoundException {
        log.info("deleting Article ID: {}", id);

        try {
            articleRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error when deleting Article from database. ", e);
            throw new ArticleNotFoundException("Article Not found");
        }
    }

    private void checkForbiddenWords(ArticleDTO articleDTO) {
        if (articleDTO.getContent().contains(blackListedWords)) {
            throw new RuntimeException("Article contains forbidden words.");
        }
    }
}
