package com.service.interfac;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.IssueDTO;
import com.entity.Book;
import com.entity.Issue;
import com.entity.Issue.IssueStatus;
import com.entity.User;
import com.exception.BadRequestException;
import com.exception.ResourceNotFoundException;
import com.repository.BookRepository;
import com.repository.IssueRepository;
import com.repository.UserRepository;
import com.service.IssueService;
import com.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final BookRepository  bookRepository;
    private final UserRepository  userRepository;

    // ── Entity → DTO ─────────────────────────────────────────────────────
    public static IssueDTO toDTO(Issue i) {
        return IssueDTO.builder()
                .issueId(i.getIssueId())
                .bookId(i.getBook().getBookId())
                .bookTitle(i.getBook().getBookTitle())
                .userId(i.getUser().getUserId())
                .userName(i.getUser().getUserName())
                .issuedDate(i.getIssuedDate())
                .dueDate(i.getDueDate())
                .returnedDate(i.getReturnedDate())
                .issueStatus(i.getStatus().name())
                .fineAmount(i.getFineAmount())
                .build();
    }
    public List<IssueDTO> getIssuesByUser_UserId(Long userId) {
        return issueRepository.findByUser_UserId(userId)
                .stream()
                .map(IssueServiceImpl::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Page<IssueDTO> getAllIssues(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("issuedDate").descending());
        if (status != null && !status.isBlank()) {
            return issueRepository.findByStatus(IssueStatus.valueOf(status), pageable)
                    .map(IssueServiceImpl::toDTO);
        }
        return issueRepository.findAll(pageable).map(IssueServiceImpl::toDTO);
    }

    @Override
    @Transactional
    public IssueDTO issueBook(Long bookId, Long userId) {
        // Validation using Stream API
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        if (book.getBookAvailableCopies() <= 0)
            throw new BadRequestException("No copies available for: " + book.getBookTitle());

        if (issueRepository.existsByUser_UserIdAndBook_BookIdAndStatus(userId, bookId, IssueStatus.ISSUED))
            throw new BadRequestException("User has already issued this book");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Decrement available copies
        book.setBookAvailableCopies(book.getBookAvailableCopies() - 1);
        bookRepository.save(book);

        Issue issue = Issue.builder()
                .book(book)
                .user(user)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))  // 2-week loan period
                .status(IssueStatus.ISSUED)
                .fineAmount(0.0)
                .build();

        return toDTO(issueRepository.save(issue));
    }

    @Override
    @Transactional
    public IssueDTO returnBook(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found: " + issueId));

        if (issue.getStatus() == IssueStatus.RETURNED)
            throw new BadRequestException("Book already returned");

        // Calculate fine using DateUtil
        double fine = DateUtil.calculateFine(issue.getDueDate(), LocalDate.now());

        issue.setReturnedDate(LocalDate.now());
        issue.setFineAmount(fine);
        issue.setStatus(IssueStatus.RETURNED);

        // Increment available copies back
        Book book = issue.getBook();
        book.setBookAvailableCopies(book.getBookAvailableCopies() + 1);
        bookRepository.save(book);

        return toDTO(issueRepository.save(issue));
    }

    @Override
    public List<IssueDTO> getOverdueIssues() {
        // Stream API: find overdue, compute fine, sort by most overdue
        return issueRepository.findOverdueIssues(LocalDate.now())
                .stream()
                .peek(i -> i.setStatus(IssueStatus.OVERDUE))    // mark overdue in memory
                .map(IssueServiceImpl::toDTO)
                .sorted((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                .collect(Collectors.toList());
    }
 // ✅ CORRECT — implement it properly:
    public List<IssueDTO> getIssuesByUser(Long userId) {
        return issueRepository.findByUser_UserIdAndStatus(userId, IssueStatus.ISSUED)
                .stream()
                .map(IssueServiceImpl::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getActiveIssuesCount() {
        return issueRepository.countActiveIssues();
    }
}