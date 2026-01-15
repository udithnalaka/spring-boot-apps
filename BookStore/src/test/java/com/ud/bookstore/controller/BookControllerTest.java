package com.ud.bookstore.controller;

import com.ud.bookstore.model.BookDTO;
import com.ud.bookstore.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBook_ShouldReturnTheBookDetails() throws Exception {

        BookDTO bookDto = BookDTO.builder()
                .bookId("100")
                .bookName("Java Design Patterns")
                .bookAuthor("Kathy")
                .price("100")
                .description("Learn Design Patterns")
                .build();

        when(bookService.getBook("100"))
                .thenReturn(bookDto);

        mockMvc.perform(get("/book-store/{bookId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId", is("100")))
                .andExpect(jsonPath("$.bookName", is("JJava Design Patterns")))
                .andExpect(jsonPath("$.bookAuthor", is("Kathy")));
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {

        mockMvc.perform(get("/book-store/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].bookId", Matchers.is("1")))
                .andExpect(jsonPath("$[0].bookName", Matchers.is("Java Essentials")))
                .andExpect(jsonPath("$[0].bookAuthor", is("Udith")))
        ;
    }

    @Test
    void addBook_ShouldAddNewBook() throws Exception {

        BookDTO newBook = BookDTO.builder()
                .bookId("4")
                .bookName("Learn Spring Boot")
                .bookAuthor("DV")
                .build();

        mockMvc.perform(post("/book-store/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("New Book added with BookID: 4"));
    }

    @Disabled
    @Test
    void updateBook() {
    }

    @Disabled
    @Test
    void deleteBook() {
    }
}