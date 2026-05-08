package com.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("SELECT b FROM Book b WHERE " +
		       "(:keyword IS NULL OR LOWER(b.bookTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(b.bookAuthor) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
		       "AND b.bookActive = true " +
		       "ORDER BY b.bookTitle ASC")
		Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);
    Page<Book> findByBookActiveTrueAndBookAvailableCopiesGreaterThan(int copies, Pageable pageable);
    List<Book> findByBookGenreAndBookActiveTrue(String genre);
    List<Book> findByBookActiveTrueAndBookAvailableCopiesLessThanEqual(int threshold);
    boolean existsByBookIsbn(String isbn);
}