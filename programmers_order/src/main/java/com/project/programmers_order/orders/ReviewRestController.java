package com.project.programmers_order.orders;


import com.project.programmers_order.security.Jwt;
import com.project.programmers_order.security.JwtAuthentication;
import com.project.programmers_order.utils.ApiUtils;
import static com.project.programmers_order.utils.ApiUtils.success;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class ReviewRestController {
    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;
    private final OrderService orderService;


    @PostMapping(path = "/{orderId}/review")
    public ApiUtils.ApiResult<Review> review(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody @Valid CreateReviewRequestDto requestDto,
            @PathVariable("orderId") Long orderId
    ) {
        return success(
                orderService.createReivew(authentication.id,orderId,requestDto)
        );
    }





}
