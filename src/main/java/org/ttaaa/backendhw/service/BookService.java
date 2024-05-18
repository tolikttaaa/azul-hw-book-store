package org.ttaaa.backendhw.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.ttaaa.backendhw.model.dto.BookDto;
import org.ttaaa.backendhw.repository.BookRepository;
import org.ttaaa.backendhw.model.entity.Book;
import org.ttaaa.backendhw.service.dto.BookDtoService;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
public class BookService {
    private BookRepository bookRepository;
    private BookDtoService bookDtoService;

    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    public Book getBook(UUID id) {
        return bookRepository.getById(id);
    }

    public Book saveBook(BookDto bookDto) {
        return bookRepository.save(bookDtoService.dtoToEntity(bookDto));
    }

    public Book updateBook(UUID id, BookDto bookDto) {
        return bookRepository.update(bookDtoService.dtoToEntity(id, bookDto));
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
