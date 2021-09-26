package com.fooqoo56.dev.stub.presentation.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooqoo56.dev.stub.presentation.dto.response.StubDtoErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    @NonNull
    public Mono<Void> handle(@NotNull final ServerWebExchange exchange,
                             @NotNull final Throwable exception) {

        final var errorResponse = getErrorResponse(exchange, exception);

        try {
            final DataBuffer db = new DefaultDataBufferFactory()
                    .wrap(objectMapper.writeValueAsBytes(errorResponse.getValue()));
            exchange.getResponse().setStatusCode(errorResponse.getKey());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);
            log.error("{} {}", exception.getMessage(), exception.getStackTrace());
            return exchange.getResponse().writeWith(Mono.just(db));
        } catch (final JsonProcessingException jsonProcessingException) {
            log.error("{} {}", exchange.getLogPrefix(), jsonProcessingException.getMessage(),
                    jsonProcessingException);
        }

        return Mono.error(exception);
    }

    /**
     * エラーレスポンス取得
     *
     * @param exchange  ServerWebExchange
     * @param exception 例外
     * @return エラーレスポンスのPair
     */
    @NonNull
    private Pair<HttpStatus, StubDtoErrorResponse> getErrorResponse(
            final ServerWebExchange exchange, final Throwable exception) {

        if (exception instanceof MethodNotAllowedException) {
            return Pair.of(HttpStatus.METHOD_NOT_ALLOWED,
                    new StubDtoErrorResponse(
                            "Method Not Allowed.",
                            "[Stub Error]許可されないメソッドでアクセスされました"));
        } else if (exception instanceof ResponseStatusException) {
            return Pair.of(HttpStatus.NOT_FOUND,
                    new StubDtoErrorResponse(
                            "Not Found.",
                            "[Stub Error]存在しないパスにアクセスされました"));
        } else {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR,
                    new StubDtoErrorResponse(
                            "Internal Server Error.",
                            "[Stub Error]想定外のエラーが発生しました"));
        }
    }
}
