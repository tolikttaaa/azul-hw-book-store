package org.ttaaa.backendhw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.service.BookService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/data/book")
@AllArgsConstructor
public class BookController {
    private BookService bookService;

    @GetMapping("/")
    public List<Book> all() {
        return bookService.getAllBooks();
    }

    @PostMapping("/")
    public Book newBook(@RequestBody @Valid BookDto dto) {
        return bookService.saveBook(dto);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable UUID id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    public Book updateAuthor(@RequestBody @Valid BookDto dto, @PathVariable UUID id) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bookService.deleteBook(id);
    }
}
