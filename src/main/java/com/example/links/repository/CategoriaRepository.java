package com.example.links.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.links.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    @Query(value = "select c from Categoria c where c.user.id = :user_id")
    List<Categoria> findAllByUserId(@Param("user_id") Long userId);

    @Query(value = "select c from Categoria c where c.id in :ids")
    List<Categoria> findAllByIds(@Param("ids") List<Long> ids);
}
