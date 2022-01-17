package com.project.programmers_order.configures.web;


import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.math.NumberUtils.toLong;
import static org.springframework.util.StringUtils.hasText;

public class SimplePageRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private static final String DEFAULT_OFFSET_PARAMETER = "offset";

    private static final String DEFAULT_SIZE_PARAMETER = "size";

    private static final long DEFAULT_OFFSET = 0;

    private static final int DEFAULT_SIZE = 5;

    private String offsetParameterName = DEFAULT_OFFSET_PARAMETER;

    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String offsetStr = webRequest.getParameter(offsetParameterName);
        String sizeStr = webRequest.getParameter(sizeParameterName);

        long offset = hasText(offsetStr) ? toLong(offsetStr) : DEFAULT_OFFSET;
        int size = hasText(sizeStr) ? toInt(sizeStr) : DEFAULT_SIZE;
        if(offset<0||size<1||size>5){
            offset=DEFAULT_OFFSET;
            size=DEFAULT_SIZE;
        }

        return new SimplePageRequest(offset,size);
    }

    public void setOffsetParameterName(String offsetParameterName) {
        this.offsetParameterName = offsetParameterName;
    }

    public void setSizeParameterName(String sizeParameterName) {
        this.sizeParameterName = sizeParameterName;
    }

}