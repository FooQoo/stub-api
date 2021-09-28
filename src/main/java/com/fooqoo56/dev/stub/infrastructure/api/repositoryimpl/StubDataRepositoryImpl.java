package com.fooqoo56.dev.stub.infrastructure.api.repositoryimpl;

import com.fooqoo56.dev.stub.domain.model.StubBody;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import com.fooqoo56.dev.stub.domain.repository.api.StubDataRepository;
import com.fooqoo56.dev.stub.domain.type.Api;
import com.fooqoo56.dev.stub.domain.type.StatusCode;
import com.fooqoo56.dev.stub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class StubDataRepositoryImpl implements StubDataRepository {

    private final WebClient stubDataClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Mono<StubBody> getSuccessStubData(final StubParam stubParam) {
        return getStubData(stubParam.getApi(), stubParam.getSuccessCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Mono<StubBody> getErrorStubData(final StubParam stubParam) {
        return getStubData(stubParam.getApi(), stubParam.getErrorCode());
    }

    /**
     * スタブデータ取得処理の共通化
     *
     * @param api        Api型インスタンス
     * @param statusCode StatusCode型インスタンス
     * @return スタブのレスポンス
     */
    @NonNull
    private Mono<StubBody> getStubData(final Api api, final StatusCode statusCode) {
        final var stubDataResponseMono = stubDataClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/{stubParam}/{status}.json")
                        .build(api.toString(), statusCode.toString()))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new NotFoundException("存在しないスタブデータです。")))
                .bodyToMono(String.class);

        return stubDataResponseMono.map(StubBody::from);
    }
}
