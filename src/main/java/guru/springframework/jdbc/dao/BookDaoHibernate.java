package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class BookDaoHibernate implements BookDao {

    private final EntityManagerFactory emf;

    @Autowired
    public BookDaoHibernate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Book> findAllBooksSortByTitle(Pageable pageable) {

        EntityManager em = getEntityManager();

        try {

            String hql = "SELECT b FROM Book b ORDER BY b.title " +
                    Objects.requireNonNull(pageable.getSort().getOrderFor("title"))
                    .getDirection().name();
            TypedQuery<Book> query = em.createQuery(hql, Book.class);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();

        } finally {
            em.close();
        }

    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {

        EntityManager em = getEntityManager();

        try {

            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());

            return query.getResultList();

        } finally {
            em.close();
        }

    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAllBooks() {

        EntityManager em = getEntityManager();

        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);

            return query.getResultList();
        } finally {
            em.clear();
        }

    }

    @Override
    public Book getById(Long id) {

        EntityManager em = getEntityManager();
        Book book = getEntityManager().find(Book.class, id);
        em.close();

        return book;

    }

    @Override
    public Book findBookByTitle(String title) {

        EntityManager em = getEntityManager();

        try {
            Query query = em.createNativeQuery("SELECT * FROM book WHERE title", Book.class);
            query.setParameter("title", title);

            return (Book) query.getSingleResult();
        } finally {
            em.close();
        }

    }

    @Override
    public Book saveNewBook(Book book) {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return book;

    }

    @Override
    public Book updateBook(Book book) {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.clear();
        Book savedBook = em.find(Book.class, book.getId());
        em.getTransaction().commit();
        em.close();

        return savedBook;

    }

    @Override
    public void deleteBookById(Long id) {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.getTransaction().commit();
        em.close();

    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
