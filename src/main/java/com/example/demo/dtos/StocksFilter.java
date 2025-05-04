package com.example.demo.dtos;

import lombok.Data;

@Data
public class StocksFilter {
    String substr;
    String availability; // all, available, outOfStock
}
