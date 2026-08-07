package com.example.spring_security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_security.entities.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
