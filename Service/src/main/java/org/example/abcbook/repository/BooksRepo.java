package org.example.abcbook.repository;

import org.example.abcbook.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepo extends BooksRepoExt, JpaRepository<Books, Long> {
}
