package com.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")        // ← "books" theek hai
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotBlank
    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @NotBlank
    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_isbn", unique = true)
    private String bookIsbn;

    @Column(name = "book_genre")
    private String bookGenre;

    @Min(0)
    @Column(name = "book_total_copies")
    private Integer bookTotalCopies;

    @Min(0)
    @Column(name = "book_available_copies")
    private Integer bookAvailableCopies;    // ← EXACTLY ye naam hona chahiye

    @Column(name = "book_publish_date")
    private LocalDate bookPublishedDate;

    private boolean bookActive = true;
 
}