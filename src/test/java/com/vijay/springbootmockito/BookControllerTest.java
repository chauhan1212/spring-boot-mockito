package com.vijay.springbootmockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vijay.springbootmockito.controller.BookController;
import com.vijay.springbootmockito.model.Book;
import com.vijay.springbootmockito.repository.BookRepository;

@ExtendWith(SpringExtension.class)
public class BookControllerTest {

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookController bookController;

	Book RECORD_1 = new Book(1L, "Atomic Habits", "How to build better babits", 3);
	Book RECORD_2 = new Book(2L, "Thinking Fast and slow", "How to create good mental health", 2);
	Book RECORD_3 = new Book(3L, "The Power of habit", "better babits", 4);

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void getAllRecords_Success() throws Exception {
		List<Book> books = new ArrayList<Book>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(bookRepository.findAll()).thenReturn(books);

		mockMvc.perform(MockMvcRequestBuilders.get("/books").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[1].name", is("Thinking Fast and slow")));
	}

	@Test
	public void getBookById_Success() throws Exception {

		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));

		mockMvc.perform(MockMvcRequestBuilders.get("/books/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Atomic Habits")));
	}

	@Test
	public void createBook_Success() throws Exception {
		Book record = Book.builder().bookId(4L).name("Introduction to C").summary("The name but longer").rating(5)
				.build();

		Mockito.when(bookRepository.save(record)).thenReturn(record);

		String content = objectWriter.writeValueAsString(record);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/books")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);

		mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Introduction to C")));
	}

	@Test
	public void updateBook_Success() throws Exception {
		Book updatedRecord = Book.builder().bookId(1L).name("Updated Book name").summary("updated summary").rating(5)
				.build();

		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
		Mockito.when(bookRepository.save(updatedRecord)).thenReturn(updatedRecord);

		String updatedContent = objectWriter.writeValueAsString(updatedRecord);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/books")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(updatedContent);

		mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Updated Book name")));
	}

	@Test
	public void deleteBookById_Success() throws Exception {

		Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));

		mockMvc.perform(MockMvcRequestBuilders.delete("/books/2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
