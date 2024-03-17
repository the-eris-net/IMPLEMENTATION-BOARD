package org.example.springimplementationboard.common;

import jakarta.servlet.http.HttpServletResponse;
import org.example.springimplementationboard.common.exception.DataNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionAdvisor {
    /**
     * instanceOf후 패턴매칭은 자버버전 16부터 지원합니다.
     * 그 이전 버전은 instanceof로 타입을 체크하고, 각 타입에 맞게 캐스팅을 해주어야 합니다.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public CommonResponse handleCustomException(HttpServletResponse response, DataNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return new CommonResponse(e.getData(), e.getMessage());
    }
}
