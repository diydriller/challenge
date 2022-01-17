package com.project.programmers_order.orders;

import com.project.programmers_order.configures.web.Pageable;
import com.project.programmers_order.security.Jwt;
import com.project.programmers_order.security.JwtAuthentication;
import com.project.programmers_order.utils.ApiUtils;
import static com.project.programmers_order.utils.ApiUtils.success;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderRestController {
    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;
    private final OrderService orderService;

    @GetMapping(path = "")
    public ApiUtils.ApiResult<List<FindOrderResponseDto>> findAll(
            @AuthenticationPrincipal JwtAuthentication authentication,
            Pageable pageable
    ) {
        return success(
                orderService.findAllOrder(authentication.id,pageable.getOffset(),pageable.getSize())
        );
    }

    @GetMapping(path = "/{orderId}")
    public ApiUtils.ApiResult<FindOrderResponseDto> findById(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return success(
                orderService.findOrder(orderId)
        );
    }

    @PatchMapping(path = "/{orderId}/accept")
    public ApiUtils.ApiResult<Boolean> accept(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return success(
               orderService.acceptOrder(orderId)
        );
    }

    @PatchMapping(path = "/{orderId}/reject")
    public ApiUtils.ApiResult<Boolean> reject(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable("orderId") Long orderId,
            @RequestBody @Valid RejectRequestDto requestDto
    ) {
        return success(
                orderService.rejectOrder(orderId,requestDto.getMessage())
        );
    }

    @PatchMapping(path = "/{orderId}/shipping")
    public ApiUtils.ApiResult<Boolean> shipping(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return success(
                orderService.shipOrder(orderId)
        );
    }

    @PatchMapping(path = "/{orderId}/complete")
    public ApiUtils.ApiResult<Boolean> complete(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return success(
                orderService.completeOrder(orderId)
        );
    }

}
