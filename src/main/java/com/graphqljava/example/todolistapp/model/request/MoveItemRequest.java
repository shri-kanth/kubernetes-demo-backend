package com.graphqljava.example.todolistapp.model.request;

import lombok.Data;

import java.util.Map;

@Data
public class MoveItemRequest {
    private Long listId;
    private int position;

    public static MoveItemRequest getMoveItemRequest(Map<String,Integer> map){
        MoveItemRequest request = new MoveItemRequest();
        request.setPosition(map.get("position"));
        request.setListId((map.get("listId").longValue()));
        return request;
    }
}
