package com.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueDTO {
    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;
    private LocalDate issuedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private String issueStatus;
    private Double fineAmount;
}