package com.project.programmers_order.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectRequestDto {
    @NotBlank(message = "message must be provided")
    private String message;
}
