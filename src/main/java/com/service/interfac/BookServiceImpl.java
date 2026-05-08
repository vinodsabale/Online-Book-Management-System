package com.service.interfac;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.BookDTO;
import com.entity.Book;
import com.exception.BadRequestException;
import com.exception.ResourceNotFoundException;
import com.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements com.service.BookService {
	
	   private final BookRepository bookRepository;

	    @Value("${app.books.low-stock-threshold:5}")
	    private int lowStockThreshold;
    // ── Convert Entity → DTO using Stream-friendly static method ──────────
    public static BookDTO toDTO(Book b) {
        return BookDTO.builder()
                .bookId(b.getBookId())
                .bookTitle(b.getBookTitle())
                .bookAuthor(b.getBookAuthor())
                .bookIsbn(b.getBookIsbn())
                .bookGenre(b.getBookGenre())
                .bookTotalCopies(b.getBookTotalCopies())
                .bookAvailableCopies(b.getBookAvailableCopies())
                .bookPublishedDate(b.getBookPublishedDate())
                .bookActive(b.isBookActive())
                .build();
    }

 
    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Book not found with id: " + id));
        book.setBookActive(false);          // soft delete
        bookRepository.save(book);
    }


    @Override
    public Page<BookDTO> getAllBooks(String keyword, int page, int size,
                                     String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.searchBooks(keyword, pageable)
                .map(BookServiceImpl::toDTO);              // Stream map on Page
    }

    @Override
	public List<String> getAllGenres() {
	    return bookRepository.findAll()
	            .stream()
	            .map(Book::getBookGenre)                          // extract genre
	            .filter(g -> g != null && !g.isBlank())       // remove null/blank
	            .distinct()                                   // unique genres
	            .sorted()                                     // alphabetical order
	            .collect(Collectors.toList());                // collect into List
	}

    @Override
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookServiceImpl::toDTO)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public List<BookDTO> getLowStockBooks() {
        // Stream API: filter, sort, collect
    	return bookRepository.findByBookActiveTrueAndBookAvailableCopiesLessThanEqual(lowStockThreshold)
                .stream()
                .filter(b -> b.getBookAvailableCopies() >= 0)
                .sorted((a, b) -> Integer.compare(a.getBookAvailableCopies(), b.getBookAvailableCopies()))
                .map(BookServiceImpl::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalBooksCount() {
        return bookRepository.count();
    }

    @Override
    @Transactional
    public BookDTO saveBook(BookDTO dto) {
    	if (bookRepository.existsByBookIsbn(dto.getBookIsbn())) {
			throw new BadRequestException("ISBN already exists: " + dto.getBookIsbn());
		}
        return toDTO(bookRepository.save(toEntity(dto)));
    }

// ── Convert DTO → Entity ───────────────────────────────────────────────
private Book toEntity(BookDTO dto) {
    return Book.builder()
            .bookTitle(dto.getBookTitle())
            .bookAuthor(dto.getBookAuthor())
            .bookIsbn(dto.getBookIsbn())
            .bookGenre(dto.getBookGenre())
            .bookTotalCopies(dto.getBookTotalCopies())
            .bookAvailableCopies(dto.getBookTotalCopies()) // initially all copies available
            .bookPublishedDate(dto.getBookPublishedDate())
            .bookActive(true)
            .build();
}

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Book not found with id: " + id));
        book.setBookTitle(dto.getBookTitle());
        book.setBookAuthor(dto.getBookAuthor());
        book.setBookIsbn(dto.getBookIsbn());
        book.setBookGenre(dto.getBookGenre());
        book.setBookTotalCopies(dto.getBookTotalCopies());
        book.setBookPublishedDate(dto.getBookPublishedDate());
        return toDTO(bookRepository.save(book));
    }
}