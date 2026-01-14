package com.ud.bookstore.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookDTO {

    @NotNull(message = "Book ID  required")
    private String bookId;

    @NotBlank(message = "Boon name required")
    @Size(min = 5)
    private String bookName;

    @NotBlank
    private String bookAuthor;
}
