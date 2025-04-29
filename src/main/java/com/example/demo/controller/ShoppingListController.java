package com.example.demo.controller;

import com.example.demo.models.ShoppingList;
import com.example.demo.service.ShoppingListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shoppinglists")
@CrossOrigin(origins = "*")  // Allow React frontend access
public class ShoppingListController {

    @Autowired
    private ShoppingListService shoppingListService;

    // POST: Save Shopping List
    @PostMapping("/save")
    public ShoppingList saveList(@RequestBody ShoppingList shoppingList) {
        return shoppingListService.saveList(shoppingList);
    }

    // GET: Get All Shopping Lists
    @GetMapping("/all")
    public List<ShoppingList> getAllLists() {
        return shoppingListService.getAllLists();
    }

    // DELETE: Delete a shopping list by ID
    @DeleteMapping("/delete/{id}")
    public String deleteList(@PathVariable String id) {
        shoppingListService.deleteList(id);
        return "Shopping list with ID " + id + " deleted successfully.";
    }
}

