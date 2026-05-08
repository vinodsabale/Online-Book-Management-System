package com.service;
import java.util.List;
import org.springframework.data.domain.Page;
import com.dto.IssueDTO;
public interface IssueService {
	 Page<IssueDTO> getAllIssues(String status, int page, int size);
	    IssueDTO issueBook(Long bookId, Long userId);
	    IssueDTO returnBook(Long issueId);
	    List<IssueDTO> getOverdueIssues();
	    List<IssueDTO> getIssuesByUser(Long userId);
	    long getActiveIssuesCount();
}