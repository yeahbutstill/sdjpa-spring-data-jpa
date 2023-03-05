package guru.springframework.jdbc;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testEmptyResultException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            Book book = bookRepository.readByTitle("foobar4");
        });
    }

    @Test
    void testNullParam() {
        Assertions.assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        Assertions.assertNull(bookRepository.getByTitle("foo"));
    }

}
