package com.graphqljava.example.todolistapp.service;

import com.graphqljava.example.todolistapp.model.db.Item;
import com.graphqljava.example.todolistapp.model.db.ListEntity;
import com.graphqljava.example.todolistapp.model.request.CreateItemRequest;
import com.graphqljava.example.todolistapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public List<Item> getItemsByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        return this.itemRepository.getItemByListEntityAndDeletedFalseOrderByPositionAsc(listEntity);
    }

    @Transactional
    public Item getItemById(Long id) throws Exception{
        assert(id != null);
        Item item = this.itemRepository.getItemByIdAndDeletedFalse(id);
        if(item == null){
            throw new Exception("Item Not Found");
        }
        return item;
    }

    @Transactional
    public Item getItemByListEntityAndPosition(ListEntity listEntity,int position){
        assert(listEntity != null);
        return this.itemRepository.getItemByListEntityAndPositionAndDeletedFalse(listEntity,position);
    }

    @Transactional
    public synchronized Item createItem(CreateItemRequest createItemRequest,ListEntity listEntity){
        assert(listEntity != null);
        Item item = new Item();
        item.setName(createItemRequest.getName());
        item.setListEntity(listEntity);
        item.setDeleted(false);
        item.setCreatedOn(new Date());
        item.setPosition(getItemCountByListEntity(listEntity).intValue());
        return saveOrUpdate(item);
    }

    @Transactional
    public void deleteItem(Item item){
        assert(item != null);
        List<Item> toBeShiftedItems = getItemsByListEntity(item.getListEntity()).stream()
                                        .filter(i -> i.getPosition() > item.getPosition())
                                        .collect(Collectors.toList());
        shiftPosition(toBeShiftedItems,-1);
        item.setDeleted(true);
        saveOrUpdate(item);
    }

    @Transactional
    public void deleteItemsByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        List<Item> itemList = getItemsByListEntity(listEntity);
        itemList.stream().forEach(item -> item.setDeleted(true));
        saveOrUpdate(itemList);
    }

    @Transactional
    private void shiftPosition(List<Item> itemList, int step){
        assert(itemList != null);
        itemList.stream().forEach(item -> item.setPosition(item.getPosition()+step));
        saveOrUpdate(itemList);
    }

    @Transactional
    public Item moveItem(Item item,ListEntity newListEntity,int newPosition) throws Exception{
        assert(item!=null && newListEntity!=null);
        Long count = getItemCountByListEntity(newListEntity);
        if(newPosition < 0 || newPosition > count){
            throw new Exception("Requested Position out of bounds");
        }
        List<Item> toBeShiftedItemsInOldListEntity = getItemsByListEntity(item.getListEntity()).stream()
                .filter(i -> i.getPosition() > item.getPosition())
                .collect(Collectors.toList());
        shiftPosition(toBeShiftedItemsInOldListEntity,-1);

        List<Item> toBeShiftedItemsInNewListEntity = getItemsByListEntity(newListEntity).stream()
                .filter(i -> i.getPosition() >= newPosition)
                .collect(Collectors.toList());
        shiftPosition(toBeShiftedItemsInNewListEntity,1);

        item.setListEntity(newListEntity);
        item.setPosition(newPosition);
        return saveOrUpdate(item);
    }

    @Transactional
    private Long getItemCountByListEntity(ListEntity listEntity){
        assert(listEntity != null);
        return this.itemRepository.countByListEntityAndDeletedFalse(listEntity);
    }

    @Transactional
    private Item saveOrUpdate(Item item){
        assert(item != null);
        item.setModifiedOn(new Date());
        return this.itemRepository.save(item);
    }

    @Transactional
    private void saveOrUpdate(List<Item> itemList){
        assert(itemList != null);
        itemList.stream().forEach(item -> item.setModifiedOn(new Date()));
        this.itemRepository.save(itemList);
    }
}
