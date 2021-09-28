package com.fooqoo56.dev.stub.application.service;

import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.domain.model.StubBody;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import com.fooqoo56.dev.stub.domain.repository.api.StubDataRepository;
import com.fooqoo56.dev.stub.domain.service.HangUpDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StubService {

    private final StubDataRepository stubDataRepository;

    private final HangUpDomainService hangUpDomainService;

    /**
     * スタブデータのエンドポイントからスタブのレスポンスを取得する
     *
     * @param stubParam  スタブのパラメータ
     * @param isDownMode サーバダウンモードの場合、true
     * @return スタブのレスポンス
     */
    @NonNull
    public Mono<StubBody> getStubBody(final StubParam stubParam, final boolean isDownMode) {
        return isDownMode ? stubDataRepository.getErrorStubData(stubParam) :
                stubDataRepository.getSuccessStubData(stubParam);

    }

    /**
     * スタブデータのエンドポイントからスタブのレスポンスを取得する
     *
     * @param stubParam スタブのパラメータ
     * @return スタブのレスポンス
     */
    @NonNull
    public Mono<StubBody> hangUpStubBody(final StubParam stubParam) {

        final var stubApiSetting = StubApiSetting.getStubApiSetting(stubParam.getApi().toString());

        hangUpDomainService.sleep(stubApiSetting.getSleepDuration());

        return stubDataRepository.getSuccessStubData(stubParam);
    }
}
