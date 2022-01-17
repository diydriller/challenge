package com.project.programmers_order.orders;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    private Long seq;
    private Long userSeq;
    private Long productSeq;
    private Long reviewSeq;
    private State state;
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createAt;


}
