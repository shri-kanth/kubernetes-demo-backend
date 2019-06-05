package com.graphqljava.example.todolistapp.model.request;

import lombok.Data;

import java.util.Map;

@Data
public class CreateItemRequest {
    private String name;
    private Long listId;

    public static CreateItemRequest getCreateItemRequest(Map map){
        CreateItemRequest request = new CreateItemRequest();
        request.setName((String) map.get("name"));
        request.setListId(Long.valueOf((Integer)map.get("listId")));
        return request;
    }
}
