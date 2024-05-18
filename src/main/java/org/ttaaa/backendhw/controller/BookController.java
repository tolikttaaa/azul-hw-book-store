package org.ttaaa.backendhw.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    public Book newBook(@RequestBody BookDto newBook) {
        return bookService.saveBook(newBook);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable UUID id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    public Book updateAuthor(@RequestBody BookDto newBook, @PathVariable UUID id) {
        return bookService.updateBook(id, newBook);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bookService.deleteBook(id);
    }
}
