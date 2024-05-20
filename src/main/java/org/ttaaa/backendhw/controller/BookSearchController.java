package org.ttaaa.backendhw.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.model.dto.BookSearchDto;
import org.ttaaa.backendhw.model.dto.PaginationResponseDto;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.service.BookService;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
@Validated
public class BookSearchController {
    private BookService bookService;

    @GetMapping("/search")
    public PaginationResponseDto<Book> searchBook(
            @RequestBody BookSearchDto dto,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        return bookService.getBookByFilter(dto, page, size);
    }
}
