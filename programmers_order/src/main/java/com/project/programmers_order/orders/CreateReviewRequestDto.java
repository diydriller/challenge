package com.project.programmers_order.orders;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {

    @Size(max=900,message = "content is too long")
    @NotBlank(message = "content must be provided")
    private String content;


}
