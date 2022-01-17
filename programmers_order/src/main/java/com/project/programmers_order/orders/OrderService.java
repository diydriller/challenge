package com.project.programmers_order.orders;

import com.project.programmers_order.errors.FailException;
import com.project.programmers_order.errors.NotFoundException;
import com.project.programmers_order.products.ProductRepository;
import com.project.programmers_order.utils.DateTimeUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Review createReivew(Long userId,Long orderId,CreateReviewRequestDto requestDto) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        if(!order.getState().equals(State.COMPLETED)){
            throw new FailException("Could not write review for order "+orderId+" because state(REQUESTED) is not allowed");
        }
        if(order.getReviewSeq()>0||order.getReviewSeq()==null){
            throw new FailException("Could not write review for order "+order.getReviewSeq()+" because have already written");
        }

        Review review=orderRepository.createReview(userId,order.getProductSeq(),requestDto.getContent());
        orderRepository.updateOrderByReviewSeq(orderId,review.getSeq());
        productRepository.updateProductByReviewCnt(order.getProductSeq());
        return review;
    }

    @Transactional
    public List<FindOrderResponseDto> findAllOrder(Long id, long offset, int size) {
        return orderRepository.findAllOrder(id,offset,size)
                .stream()
                .map(o->FindOrderResponseDto.builder()
                        .seq(o.getSeq())
                        .review(orderRepository.findReviewById(o.getReviewSeq()))
                        .productId(o.getProductSeq())
                        .state(o.getState())
                        .rejectMessage(o.getRejectMsg())
                        .requestMessage(o.getRequestMsg())
                        .completedAt(o.getCompletedAt())
                        .rejectedAt(o.getRejectedAt())
                        .createAt(o.getCreateAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public FindOrderResponseDto findOrder(Long orderId) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        Review review=null;
        if(order.getReviewSeq()>0){
            review=orderRepository.findReviewById(order.getReviewSeq());
        }

        return FindOrderResponseDto.builder()
                .seq(order.getSeq())
                .productId(order.getProductSeq())
                .review(review)
                .state(order.getState())
                .requestMessage(order.getRequestMsg())
                .rejectMessage(order.getRejectMsg())
                .completedAt(order.getCompletedAt())
                .rejectedAt(order.getRejectedAt())
                .createAt(order.getCreateAt())
                .build();
    }

    @Transactional
    public Boolean acceptOrder(Long orderId) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        Boolean result=false;
        if(order.getState().equals(State.REQUESTED)){
            result=true;
            orderRepository.updateOrderByState(orderId,State.ACCEPTED);
        }
        return result;
    }

    @Transactional
    public Boolean rejectOrder(Long orderId,String message) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        Boolean result=false;
        if(order.getState().equals(State.REQUESTED)){
            result=true;
            orderRepository.updateOrderByStateAndMsg(orderId,State.REJECTED,message);
            Timestamp time= DateTimeUtils.toTimestamp(LocalDateTime.now());
            orderRepository.updateOrderByStateAndRejectTime(orderId,State.REJECTED,time);
        }
        return result;
    }

    @Transactional
    public Boolean shipOrder(Long orderId) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        Boolean result=false;
        if(order.getState().equals(State.ACCEPTED)){
            result=true;
            orderRepository.updateOrderByState(orderId,State.SHIPPING);
        }
        return result;
    }

    @Transactional
    public Boolean completeOrder(Long orderId) {
        Orders order=orderRepository.findOrderById(orderId)
                .orElseThrow(()->new NotFoundException("Could not found order for "+orderId));

        Boolean result=false;
        if(order.getState().equals(State.SHIPPING)){
            result=true;
            Timestamp time= DateTimeUtils.toTimestamp(LocalDateTime.now());
            orderRepository.updateOrderByStateAndCompleteTime(orderId,State.COMPLETED,time);
        }
        return result;
    }
}
