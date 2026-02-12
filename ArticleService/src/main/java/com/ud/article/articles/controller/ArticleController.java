package com.ud.article.articles.controller;

import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long articleId) {
        log.info("Article ID: {}", articleId);
        return ResponseEntity.ok(articleService.getArticleById(articleId));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByTitle(@PathVariable String title) {
        log.info("Article Title: {}", title);

        return ResponseEntity.ok(articleService.getArticleByTitle(title));
    }

    @PostMapping("/")
    public ResponseEntity<ArticleDTO> saveArticle(@RequestBody ArticleDTO articleDTO) {
        log.info("Article DTO: {}", articleDTO);

        return ResponseEntity.ok(articleService.saveArticle(articleDTO));
    }

    @DeleteMapping("/{articleId}")
    public void deleteArticleById(@PathVariable Long articleId) {
        log.info("Article ID: {}", articleId);

        articleService.deleteArticle(articleId);
    }

}
