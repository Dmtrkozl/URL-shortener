package com.example.shortener.repo;

import com.example.shortener.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepo extends JpaRepository<Link, Long> {
    Link findByHash(String hash);

    List<Link> findByUserId(Long userId);

    Link findByUrl(String url);
}
