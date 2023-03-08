package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoJDBCTemplate;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@Testcontainers
class AuthorDaoJDBCTemplateTest {

    @Container
    public static PostgreSQLContainer<?> pgsql = new PostgreSQLContainer<>("postgres:14");

    @DynamicPropertySource
    static void configureTestContainersProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", pgsql::getJdbcUrl);
        registry.add("spring.datasource.username", pgsql::getUsername);
        registry.add("spring.datasource.password", pgsql::getPassword);

    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Qualifier("authorDaoJDBCTemplate")
    @Autowired
    AuthorDao authorDao;

    @BeforeEach
    void setup() {
        authorDao = new AuthorDaoJDBCTemplate(jdbcTemplate);
    }

    @Test
    void testFindAllAuthorByLastName() {

        List<Author> smith = authorDao.findAllAuthorsByLastName("Smith", PageRequest.of(0, 10));

        assertThat(smith).isNotNull();
        assertThat(smith.size()).isEqualTo(10);

    }

    @Test
    void testFindAllAuthorByLastNameSortLastNameDesc() {

        List<Author> smith = authorDao.findAllAuthorsByLastName("Smith",
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("firstname"))));

        assertThat(smith).isNotNull();
        assertThat(smith.size()).isEqualTo(10);
        assertThat(smith.get(0).getFirstName()).isEqualTo("Yugal");

    }

    @Test
    void testFindAllAuthorByLastNameSortLastNameAsc() {

        List<Author> smith = authorDao.findAllAuthorsByLastName("Smith",
                PageRequest.of(0, 10, Sort.by(Sort.Order.asc("firstname"))));

        assertThat(smith).isNotNull();
        assertThat(smith.size()).isEqualTo(10);
        assertThat(smith.get(0).getFirstName()).isEqualTo("Ahmed");

    }

    @Test
    void testFindAllAuthorByLastNameSortLastNameResc() {

        List<Author> smith = authorDao.findAllAuthorsByLastName("Smith", PageRequest.of(0, 100));

        assertThat(smith).isNotNull();
        assertThat(smith.size()).isEqualTo(40);

    }

}