package com.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(BadRequestException.class)
   public ResponseEntity<Map<String,Object>>handleBadRequest(BadRequestException ex,HttpServletRequest req){
	   if(req.getRequestURI().startsWith("/api/")) {
		   Map<String,Object> body=Map.of("error","Bad Request","message",ex.getMessage(),"status",400);
		   return ResponseEntity.badRequest().body(body);
	   }
	   return ResponseEntity.badRequest().body(Map.of("message",ex.getMessage()));
   }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage());
        return "common/error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "common/error";
    }
}