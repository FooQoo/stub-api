package com.fooqoo56.dev.stub.presentation.controller;

import com.fooqoo56.dev.stub.application.service.StubService;
import com.fooqoo56.dev.stub.config.StubConfig;
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.domain.model.StubBody;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import com.fooqoo56.dev.stub.domain.service.ResponseConfigDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequestMapping(value = "/stub/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j
public class StubController {

    private final StubService stubService;

    private final StubConfig stubConfig;

    private final ResponseConfigDomainService responseConfigDomainService;

    /**
     * GETメソッドでスタブを返却する
     *
     * @param api         対象とするAPI
     * @param successCode 成功時のステータスコード
     * @param errorCode   失敗時のステータスコード
     * @param exchange    ServerWebExchange
     * @return JSON文字列
     */
    @GetMapping(path = "{api}/{successCode}/{errorCode}/*")
    public Mono<String> getStubForGet(@PathVariable("api") final String api,
                                      @PathVariable("successCode") final String successCode,
                                      @PathVariable("errorCode") final String errorCode,
                                      final ServerWebExchange exchange
    ) {
        final var stubParam = StubParam.of(api, successCode, errorCode);

        final var stubData =
                stubConfig.isTimeoutMode() ? stubService.hangUpStubBody(stubParam) :
                        stubService.getStubBody(stubParam,
                                stubConfig.isDownMode());

        setResponse(stubParam, exchange);

        return stubData.map(StubBody::toString);
    }

    /**
     * Response情報をセットする
     *
     * @param stubParam スタブのパラメータ
     * @param exchange  ServerWebExchange
     */
    private void setResponse(final StubParam stubParam, final ServerWebExchange exchange) {
        final var stubApiSetting = StubApiSetting.getStubApiSetting(stubParam.getApi().toString());

        exchange.getResponse()
                .setRawStatusCode(responseConfigDomainService.getRawStatusCode(stubParam));

        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.CONTENT_TYPE,
                        responseConfigDomainService.getMediaTypeString(stubApiSetting));
    }
}
