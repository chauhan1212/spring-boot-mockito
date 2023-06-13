package com.vijay.springbootmockito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.springbootmockito.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
}
