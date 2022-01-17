package com.project.programmers_order.orders;

import lombok.*;
import java.time.LocalDateTime;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review{

    private Long seq;
    private Long productId;
    private String content;
    private LocalDateTime createAt;
}
