package com.example.links.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.links.entity.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
   @Query(value = "select l from Link l where l.user.id = :userId")
   List<Link> findAllByUserId(Long userId); 
}
