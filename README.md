# Spring Data JPA - Spring Data JPA

This repository contains source code examples to support my course Spring Data JPA and Hibernate Beginner to Guru

## Connect with Spring Framework Guru
* Spring Framework Guru [Blog](https://springframework.guru/)
* Subscribe to Spring Framework Guru on [YouTube](https://www.youtube.com/channel/UCrXb8NaMPQCQkT8yMP_hSkw)
* Like Spring Framework Guru on [Facebook](https://www.facebook.com/springframeworkguru/)
* Follow Spring Framework Guru on [Twitter](https://twitter.com/spring_guru)
* Connect with John Thompson on [LinkedIn](http://www.linkedin.com/in/springguru)

## PostgreSQL Docker Setup
```shell
docker run --rm \
--name book-db2 \
-e POSTGRES_DB=bookdb2 \
-e POSTGRES_USER=sdjpa \
-e POSTGRES_PASSWORD=PNSJkxXvVNDAhePMuExTBuRR \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v "$PWD/bookdb2-data:/var/lib/postgresql/data" \
-p 5432:5432 \
postgres:14

```

## Login PostgreSQL
```shell
psql -h 127.0.0.1 -U sdjpa bookdb2
```

## Create User
```sql
CREATE USER bookadmin SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN REPLICATION PASSWORD 'password';
CREATE USER bookuser CREATEDB CREATEROLE INHERIT LOGIN REPLICATION PASSWORD 'password';
```

## List Book Author
```sql
SELECT author.id AS id, first_name, last_name, book.id AS book_id, book.isbn, book.publisher, book.title FROM author LEFT OUTER JOIN book ON author.id = book.author_id WHERE author.id = 1;
```

## Run with profile and skip test
```shell
mvn clean install spring-boot:run -Dspring-boot.run.profiles=local -DskipTests
```

## Run Sonarqube
```shell
docker run --rm --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:lts-community
```
Once your instance is up and running, Log in to http://localhost:9000 using System Administrator credentials:
- login: admin
- password: admin

details
https://docs.sonarqube.org/latest/try-out-sonarqube/