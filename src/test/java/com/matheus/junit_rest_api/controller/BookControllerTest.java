package com.matheus.junit_rest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.matheus.junit_rest_api.model.Book;
import com.matheus.junit_rest_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Atomic Habits", "How to build better habits", 5);
    Book RECORD_2 = new Book(2L, "Thinking Fast and Slow", "How to create good mental models", 4);
    Book RECORD_3 = new Book(3L, "Grokking Algorithms", "Learn algorithms the fun way", 5);

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @DisplayName("Should return all records")
    public void getAllRecords_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(bookRepository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Grokking Algorithms")));

    }

    @Test
    @DisplayName("Should return book record by its id")
    public void getBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Atomic Habits")));
    }

    @Test
    @DisplayName("Should return NotFoundStatus while getting Book by Id")
    public void getBookById_failure() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_1.getId())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should return book record")
    public void createBookRecord_success() throws Exception {
        Book record = Book.builder().id(4L).name("Introduction to C").summary("The name but longer").rating(5).build();
        Mockito.when(bookRepository.save(record)).thenReturn(record);
        String content = objectWriter.writeValueAsString(record);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Introduction to C")));
    }

    @Test
    @DisplayName("Should return BadRequestStatus while trying to create book with empty name ")
    public void createBookRecord_failure() throws Exception {
        Book record = Book.builder().id(4L).name("").summary("The name but longer").rating(5).build();
        String content = objectWriter.writeValueAsString(record);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return updated book record")
    public void updateBookRecord_success() throws Exception {
        Book updateRecord = Book.builder().id(1L).name("Updated Book Name").summary("Updated Summary").rating(1).build();
        Mockito.when(bookRepository.findById(RECORD_1.getId())).thenReturn(Optional.of(RECORD_1));
        Mockito.when(bookRepository.save(updateRecord)).thenReturn(updateRecord);
        String content = objectWriter.writeValueAsString(updateRecord);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Updated Book Name")));
    }

    @Test
    @DisplayName("Should return BadRequestStatus while trying to update book with empty name")
    public void updateBookRecord_failure_validation() throws Exception {
        Book updateRecord = Book.builder().id(1L).name("").summary("Updated Summary").rating(1).build();
        String content = objectWriter.writeValueAsString(updateRecord);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return NotFoundStatus while trying to update book")
    public void updateBookRecord_failure_notFound() throws Exception {
        Book updateRecord = Book.builder().id(1L).name("Updated Name").summary("Updated Summary").rating(1).build();
        String content = objectWriter.writeValueAsString(updateRecord);
        Mockito.when(bookRepository.findById(RECORD_1.getId())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return OkStatus")
    public void deleteBookById_success() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_2.getId())).thenReturn(Optional.of(RECORD_2));
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return NotFoundStatus while trying to delete book")
    public void deleteBookById_failure() throws Exception {
        Mockito.when(bookRepository.findById(RECORD_2.getId())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
