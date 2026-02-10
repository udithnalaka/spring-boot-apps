package com.ud.article.articles.service;

import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.exception.ArticleNotFoundException;

import java.util.List;

public interface ArticleService {

    ArticleDTO getArticleById(Long id);
    List<ArticleDTO> getArticleByTitle(String title);
    Long saveArticle(ArticleDTO articleDTO);
    Long updateArticle(Long id, ArticleDTO articleDTO);
    void deleteArticle(Long id) throws ArticleNotFoundException;

}
