package org.bpm.abcbook.repository;

import org.bpm.abcbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
