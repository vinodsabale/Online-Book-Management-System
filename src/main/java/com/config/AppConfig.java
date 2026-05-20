package com.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.entity.Book;
import com.entity.User;
import com.repository.BookRepository;
import com.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedData(BookRepository bookRepo, UserRepository userRepo) {
        return args -> {
            if (bookRepo.count() == 0) {
                bookRepo.save(Book.builder().bookTitle("Effective Java").bookAuthor("Joshua Bloch")
                        .bookIsbn("978-0134685991").bookGenre("Technology")
                        .bookTotalCopies(10).bookAvailableCopies(10)
                        .bookPublishedDate(LocalDate.of(2018, 1, 6)).bookActive(true).build());

                bookRepo.save(Book.builder().bookTitle("Clean Code").bookAuthor("Robert C. Martin")
                        .bookIsbn("978-0132350884").bookGenre("Technology")
                        .bookTotalCopies(8).bookAvailableCopies(8)
                        .bookPublishedDate(LocalDate.of(2008, 8, 1)).bookActive(true).build());

                bookRepo.save(Book.builder().bookTitle("The Alchemist").bookAuthor("Paulo Coelho")
                        .bookIsbn("978-0062315007").bookGenre("Fiction")
                        .bookTotalCopies(15).bookAvailableCopies(3)
                        .bookPublishedDate(LocalDate.of(1988, 1, 1)).bookActive(true).build());
            }
            // user seed काढला — आता register करूनच येणार
        };
    }
}