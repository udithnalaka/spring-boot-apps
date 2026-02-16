package com.ud.article.articles.controller;

import com.ud.article.articles.dto.ArticleDTO;
import com.ud.article.articles.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Article Management", description = "APIs for managing articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Operation(summary = "Get article by ID", description = "Retrieve a specific article by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/{articleId}")
    @PreAuthorize("hasAuthority('SCOPE_readarticle')")
    public ResponseEntity<ArticleDTO> getArticleById(
            @Parameter(description = "ID of the article to retrieve", required = true)
            @PathVariable Long articleId) {
        log.info("Article ID: {}", articleId);
        return ResponseEntity.ok(articleService.getArticleById(articleId));
    }

    @Operation(summary = "Get articles by title", description = "Search for articles by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Articles retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class)))
    })
    @GetMapping("/title/{title}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByTitle(
            @Parameter(description = "Title of the article to search", required = true)
            @PathVariable String title) {
        log.info("Article Title: {}", title);

        return ResponseEntity.ok(articleService.getArticleByTitle(title));
    }

    @Operation(summary = "Create a new article", description = "Save a new article to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_createarticle')")
    public ResponseEntity<ArticleDTO> saveArticle(
            @Parameter(description = "Article data to be created", required = true)
            @RequestBody ArticleDTO articleDTO) {
        log.info("Article DTO: {}", articleDTO);

        return ResponseEntity.ok(articleService.saveArticle(articleDTO));
    }

    @Operation(summary = "Delete article by ID", description = "Remove an article from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping("/{articleId}")
    @PreAuthorize("hasAuthority('SCOPE_deletearticle')")
    public void deleteArticleById(
            @Parameter(description = "ID of the article to delete", required = true)
            @PathVariable Long articleId) {
        log.info("Article ID: {}", articleId);

        articleService.deleteArticle(articleId);
    }

}
