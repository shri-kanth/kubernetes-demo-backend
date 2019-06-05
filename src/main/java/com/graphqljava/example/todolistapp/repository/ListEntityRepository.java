package com.graphqljava.example.todolistapp.repository;

import com.graphqljava.example.todolistapp.model.db.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListEntityRepository extends CrudRepository<ListEntity,Long> {
    List<ListEntity> getListEntityByDeletedFalseOrderByPositionAsc();
    Long countListEntityByDeletedFalse();
    ListEntity getListEntityByIdAndDeletedFalse(Long id);
    ListEntity getListEntityByPositionAndDeletedFalse(int position);
}
