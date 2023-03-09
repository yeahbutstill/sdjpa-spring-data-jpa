package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@Testcontainers
class BookDaoImplSpringDataJPATest {

    @Container
    public static PostgreSQLContainer<?> pgsql = new PostgreSQLContainer<>("postgres:14");
    @Qualifier("bookDaoImpl")
    @Autowired
    BookDao bookDao;

    @DynamicPropertySource
    static void configureTestContainerProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", pgsql::getJdbcUrl);
        registry.add("spring.datasource.username", pgsql::getUsername);
        registry.add("spring.datasource.password", pgsql::getPassword);

    }

    @Test
    void findAllBooksPage1_SortByTitle() {
        List<Book> books = bookDao.findAllBooksSortByTitle(PageRequest.of(0, 10,
                Sort.by(Sort.Order.desc("title"))));

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).hasSize(10);
    }

    @Test
    void findAllBooksPage1_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(0, 10));

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).hasSize(10);
    }

    @Test
    void findAllBooksPage2_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(1, 10));

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void findAllBooksPage10_pageable() {
        List<Book> books = bookDao.findAllBooks(PageRequest.of(10, 10));

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void findAllBooksPage1() {
        List<Book> books = bookDao.findAllBooks(10, 0);

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).hasSize(10);
    }

    @Test
    void findAllBooksPage2() {
        List<Book> books = bookDao.findAllBooks(10, 10);

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void findAllBooksPage10() {
        List<Book> books = bookDao.findAllBooks(10, 100);

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void testFindAllBooks() {
        List<Book> books = bookDao.findAllBooks();

        AssertionsForClassTypes.assertThat(books).isNotNull();
        assertThat(books).hasSizeGreaterThan(5);
    }

    @Test
    void getById() {
        Book book = bookDao.getById(3L);

        AssertionsForClassTypes.assertThat(book.getId()).isNotNull();
    }

    @Test
    void findBookByTitle() {
        Book book = bookDao.findBookByTitle("Clean Code");

        AssertionsForClassTypes.assertThat(book).isNotNull();
    }

    @Test
    void saveNewBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);

        Book saved = bookDao.saveNewBook(book);

        AssertionsForClassTypes.assertThat(saved).isNotNull();
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        AssertionsForClassTypes.assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void deleteBookById() {

        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            bookDao.getById(saved.getId());
        });
    }

}