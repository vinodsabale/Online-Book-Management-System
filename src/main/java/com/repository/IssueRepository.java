package com.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.Issue;
import com.entity.Issue.IssueStatus;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    Page<Issue> findByStatus(IssueStatus status, Pageable pageable);

    List<Issue> findByUser_UserIdAndStatus(Long userId, IssueStatus status);

    @Query("SELECT i FROM Issue i WHERE i.dueDate < :today AND i.status = 'ISSUED'")
    List<Issue> findOverdueIssues(@Param("today") LocalDate today);

    @Query("SELECT i FROM Issue i WHERE i.book.bookId = :bookId AND i.status = 'ISSUED'")
    List<Issue> findActiveIssuesByBook(@Param("bookId") Long bookId);

    boolean existsByUser_UserIdAndBook_BookIdAndStatus(Long userId, Long bookId, IssueStatus status);

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = 'ISSUED'")
    long countActiveIssues();

    List<Issue> findByUser_UserId(Long userId);
 
}