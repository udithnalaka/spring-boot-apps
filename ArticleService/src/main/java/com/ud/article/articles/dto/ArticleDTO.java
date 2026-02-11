package com.ud.article.articles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private List<TagDTO> tags;

}


