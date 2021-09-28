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

        final var errorResponse = getErrorResponse(exception);

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        try {
            final DataBuffer db = new DefaultDataBufferFactory()
                    .wrap(objectMapper.writeValueAsBytes(errorResponse.getValue()));

            exchange.getResponse().setStatusCode(errorResponse.getKey());
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
     * @param exception 例外
     * @return エラーレスポンスのPair
     */
    @NonNull
    private static Pair<HttpStatus, StubDtoErrorResponse> getErrorResponse(
            final Throwable exception) {

        if (exception instanceof MethodNotAllowedException) {
            return Pair
                    .of(HttpStatus.METHOD_NOT_ALLOWED, StubDtoErrorResponse.getMethodNotAllowed());
        } else if (exception instanceof ResponseStatusException) {
            return Pair.of(HttpStatus.NOT_FOUND,
                    StubDtoErrorResponse.getNotFound("存在しないパスにアクセスされました"));
        } else {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR,
                    StubDtoErrorResponse.getInternalServerError());
        }
    }
}
