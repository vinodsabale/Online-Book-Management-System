package com.controller;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.BookDTO;
import com.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    // GET /api/books?keyword=&page=0&size=5&sortBy=bookTitle&sortDir=asc
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "bookTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(bookService.getAllBooks(keyword, page, size, sortBy, sortDir));
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.saveBook(dto));
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                               @Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    // GET /api/books/low-stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<BookDTO>> getLowStock() {
        return ResponseEntity.ok(bookService.getLowStockBooks());
    }

    // GET /api/books/genres
    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(bookService.getAllGenres());
    }
}