package com.vijay.springbootmockito.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.springbootmockito.model.Book;
import com.vijay.springbootmockito.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@GetMapping
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/{bookId}")
	public Book getBookById(@PathVariable("bookId") long bookId) {
		return bookRepository.findById(bookId).get();
	}

	@PostMapping
	public Book createBook(@RequestBody @Validated Book book) {
		return bookRepository.save(book);
	}

	@PutMapping
	public Book updateBook(@RequestBody @Validated Book book) throws Exception {

		if (book == null || book.getBookId() == null) {
			throw new Exception("Book id should not be null");
		}
		Optional<Book> optionalBook = bookRepository.findById(book.getBookId());

		if (!optionalBook.isPresent()) {
			throw new Exception("Book not found");
		}

		return bookRepository.save(book);
	}

	@DeleteMapping("/{bookId}")
	public void deleteBookById(@PathVariable("bookId") long bookId) throws Exception {
		if (!bookRepository.findById(bookId).isPresent()) {
			throw new Exception("BookId: " + bookId + " not present");
		}
		bookRepository.deleteById(bookId);
	}

}
