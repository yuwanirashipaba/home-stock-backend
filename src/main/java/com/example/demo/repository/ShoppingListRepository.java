package com.example.demo.repository;

import com.example.demo.models.ShoppingList;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShoppingListRepository extends MongoRepository<ShoppingList, String> {
}
