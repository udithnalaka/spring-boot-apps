package com.ud.article.articles.repository;

import com.ud.article.articles.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);
}
