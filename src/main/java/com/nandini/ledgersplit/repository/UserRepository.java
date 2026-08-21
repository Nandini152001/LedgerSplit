package com.nandini.ledgersplit.repository;

import com.nandini.ledgersplit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
