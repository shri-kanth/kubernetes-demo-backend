package com.graphqljava.example.todolistapp.repository;

import com.graphqljava.example.todolistapp.model.db.Item;
import com.graphqljava.example.todolistapp.model.db.ListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item,Long> {
    List<Item> getItemByListEntityAndDeletedFalseOrderByPositionAsc(ListEntity listEntity);
    Long countByListEntityAndDeletedFalse(ListEntity listEntity);
    Item getItemByIdAndDeletedFalse(Long id);
    Item getItemByListEntityAndPositionAndDeletedFalse(ListEntity listEntity,int position);
}
