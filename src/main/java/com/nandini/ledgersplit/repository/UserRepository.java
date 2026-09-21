package com.nandini.ledgersplit.repository;

import com.nandini.ledgersplit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}