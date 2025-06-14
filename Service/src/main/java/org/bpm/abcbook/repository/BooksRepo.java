package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepo extends BooksRepoExt, JpaRepository<Books, Long> {
}
