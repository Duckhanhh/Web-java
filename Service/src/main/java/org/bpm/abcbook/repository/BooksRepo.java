package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepo extends BooksRepoExt, JpaRepository<Books, Long> {
}
