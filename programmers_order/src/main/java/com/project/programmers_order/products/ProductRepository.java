package com.project.programmers_order.products;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(long id);

    List<Product> findAll();
    void updateProductByReviewCnt(Long id);
}