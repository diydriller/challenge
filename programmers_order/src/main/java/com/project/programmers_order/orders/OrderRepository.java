package com.project.programmers_order.orders;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Orders> findOrderById(Long id);
    Review createReview(Long userId,Long productId,String content);
    void updateOrderByReviewSeq(Long id,Long reviewSeq);
    List<Orders> findAllOrder(Long userId,Long offset,int size);
    Review findReviewById(Long id);
    void updateOrderByState(Long id,State state);
    void updateOrderByStateAndMsg(Long id,State state,String message);
    void updateOrderByStateAndCompleteTime(Long id, State state, Timestamp time);
    void updateOrderByStateAndRejectTime(Long id, State state, Timestamp time);
}
