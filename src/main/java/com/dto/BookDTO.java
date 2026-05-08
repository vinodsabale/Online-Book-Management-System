package com.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long bookId;
    @NotBlank(message = "Title is required")
    private String bookTitle;
    @NotBlank(message = "Author is required")
    private String bookAuthor;
    private String bookIsbn;
    private String bookGenre;
    @Min(value = 0, message = "Total copies must be >= 0")
    private Integer bookTotalCopies;
    private Integer bookAvailableCopies;
    private LocalDate bookPublishedDate;
    private boolean bookActive=true;
}