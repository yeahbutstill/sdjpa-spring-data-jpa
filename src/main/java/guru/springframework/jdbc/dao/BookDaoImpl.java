package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDaoImpl implements BookDao {

    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAllBooks() {
        return null;
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.getReferenceById(id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return bookRepository.findBookByTitle(title).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(Book book) {
        Book foundBook = bookRepository.getReferenceById(book.getId());
        foundBook.setIsbn(book.getIsbn());
        foundBook.setPublisher(book.getPublisher());
        foundBook.setAuthorId(book.getAuthorId());
        foundBook.setTitle(book.getTitle());

        return bookRepository.save(foundBook);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

}
