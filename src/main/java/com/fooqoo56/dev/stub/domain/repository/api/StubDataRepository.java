package com.fooqoo56.dev.stub.domain.repository.api;

import com.fooqoo56.dev.stub.domain.model.StubBody;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import reactor.core.publisher.Mono;

public interface StubDataRepository {

    /**
     * 正常系スタブデータの取得
     *
     * @param stubParam スタブのパラメータ
     * @return スタブのレスポンス
     */
    Mono<StubBody> getSuccessStubData(final StubParam stubParam);

    /**
     * 異常系スタブデータの取得
     *
     * @param stubParam スタブのパラメータ
     * @return スタブのレスポンス
     */
    Mono<StubBody> getErrorStubData(final StubParam stubParam);
}
