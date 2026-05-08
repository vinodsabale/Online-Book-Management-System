package com.controller;

import com.dto.IssueDTO;
import com.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueRestController {

    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<Page<IssueDTO>> getAllIssues(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(issueService.getAllIssues(status, page, size));
    }

    // POST /api/issues?bookId=1&userId=2
    @PostMapping
    public ResponseEntity<IssueDTO> issueBook(@RequestParam Long bookId,
                                               @RequestParam Long userId) {
        return ResponseEntity.ok(issueService.issueBook(bookId, userId));
    }

    // PUT /api/issues/{id}/return
    @PutMapping("/{id}/return")
    public ResponseEntity<IssueDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.returnBook(id));
    }

    // GET /api/issues/overdue
    @GetMapping("/overdue")
    public ResponseEntity<List<IssueDTO>> getOverdue() {
        return ResponseEntity.ok(issueService.getOverdueIssues());
    }

    // GET /api/issues/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IssueDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(issueService.getIssuesByUser(userId));
    }
}