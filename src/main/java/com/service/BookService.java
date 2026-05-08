package com.service;
import java.util.List;

import org.springframework.data.domain.Page;

import com.dto.BookDTO;
public interface BookService {
	   Page<BookDTO> getAllBooks(String keyword, int page, int size, String sortBy, String sortDir);
	    BookDTO getBookById(Long id);
	    BookDTO saveBook(BookDTO bookDTO);
	    BookDTO updateBook(Long id, BookDTO bookDTO);
	    void deleteBook(Long id);
	    List<BookDTO> getLowStockBooks();
	    List<String> getAllGenres();
	    long getTotalBooksCount();
}