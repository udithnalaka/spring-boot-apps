package com.ud.article.ArticleService.repository;

import com.ud.article.ArticleService.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);
}
