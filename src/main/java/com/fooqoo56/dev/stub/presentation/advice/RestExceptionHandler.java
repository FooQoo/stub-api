package com.fooqoo56.dev.stub.presentation.advice;

import com.fooqoo56.dev.stub.exception.BadRequestException;
import com.fooqoo56.dev.stub.exception.NotFoundException;
import com.fooqoo56.dev.stub.presentation.controller.StubController;
import com.fooqoo56.dev.stub.presentation.dto.response.StubDtoErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice(basePackageClasses = StubController.class)
@Slf4j
public class RestExceptionHandler {

    /**
     * 入力エラー
     *
     * @param exception BadRequestException
     * @return エラーレスポンス
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<StubDtoErrorResponse> handleBadRequestException(final ServerWebExchange exchange,
                                                                final BadRequestException exception) {
        log.error(exception.getMessage());
        setContentType(exchange);
        return Mono.just(StubDtoErrorResponse.getBadRequest(exception.getMessage()));
    }

    /**
     * 404
     *
     * @param exception NotFoundException
     * @return エラーレスポンス
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Mono<StubDtoErrorResponse> handleException(final ServerWebExchange exchange,
                                                      final NotFoundException exception) {
        log.error(exception.getMessage());
        setContentType(exchange);
        return Mono
                .just(StubDtoErrorResponse.getNotFound(exception.getMessage()));
    }

    /**
     * その他のエラー
     *
     * @param exception Throwable
     * @return エラーレスポンス
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Mono<StubDtoErrorResponse> handleException(final ServerWebExchange exchange,
                                                      final Throwable exception) {
        log.error(exception.getMessage());
        setContentType(exchange);
        return Mono
                .just(StubDtoErrorResponse.getInternalServerError());
    }

    /**
     * ContentTypeをセットする
     *
     * @param exchange ServerWebExchange
     */
    private void setContentType(final ServerWebExchange exchange) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);
    }
}
