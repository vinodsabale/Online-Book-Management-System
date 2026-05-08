package com.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="issue")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Issue {
	public enum IssueStatus { ISSUED, RETURNED, OVERDUE }
	  @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long issueId;
	    @ManyToOne(fetch = FetchType.LAZY)  //Many issues can be linked to one book.fetch = FetchType.LAZY->Book data is loaded only when accessed (performance optimization).
		    @JoinColumn(name = "book_id", nullable = false)//Foreign key column book_id in issues table, must not be null.
		    private Book book;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
	    private LocalDate issuedDate;
	    private LocalDate dueDate;
	    private LocalDate returnedDate;
	    @Enumerated(EnumType.STRING)   //Stores enum values (ISSUED, RETURNED, OVERDUE) as strings in DB.
	    private IssueStatus status = IssueStatus.ISSUED;
	    private Double fineAmount = 0.0;
	}
