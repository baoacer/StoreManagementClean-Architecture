package com.example.addProduct;

public interface AddProductOutputBoundary {
    void presenter(AddProductOutputDTO addProductOutputDTO);
    void exportResult(AddProductOutputDTO addProductOutputDTO);
}