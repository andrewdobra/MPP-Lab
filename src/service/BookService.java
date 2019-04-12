package service;

import domain.BaseEntity;
import domain.Book;
import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author radu.
 */
public class BookService {
    private Repository<Long, Book> repository;

    public BookService(Repository<Long, Book> repository) {
        this.repository = repository;
    }

    public void addBook(Book book) throws ValidatorException {
        repository.save(book);
    }

    public void delBook(Book book) throws ValidatorException {
        repository.delete(book.getId());
    }

    public Set<Book> getAllBooks() {
        Iterable<Book> students = repository.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Returns all students whose name contain the given string.
     *
     * @param s
     * @return
     */
    public Set<Book> filterBooksByName(String s) {
        Iterable<Book> students = repository.findAll();
        //version 1
//        Set<Book> filteredBooks = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<Book> filteredBooks= new HashSet<>();
        students.forEach(filteredBooks::add);
        filteredBooks.removeIf(student -> !student.getName().contains(s));

        return filteredBooks;
    }
}