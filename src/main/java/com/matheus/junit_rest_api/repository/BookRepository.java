package com.matheus.junit_rest_api.repository;

import com.matheus.junit_rest_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
