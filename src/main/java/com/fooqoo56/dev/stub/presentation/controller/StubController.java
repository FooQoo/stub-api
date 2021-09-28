package com.fooqoo56.dev.stub.presentation.controller;

import com.fooqoo56.dev.stub.application.service.StubService;
import com.fooqoo56.dev.stub.config.StubConfig;
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.domain.model.StubBody;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import com.fooqoo56.dev.stub.domain.service.ResponseConfigDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "stub", description = "Stub REST API")
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
    @Operation(summary = "GETメソッドのスタブAPI",
            description = "GETメソッドでスタブデータを取得する")
    @GetMapping(path = "{api}/{successCode}/{errorCode}/*")
    public Mono<String> getStubForGet(
            @Parameter(
                    required = true,
                    description = "取得するスタブデータのキーワード<br>"
                            + "・登録されたキーワード以外が入力された場合、400エラーになる<br>"
                            + "・スタブデータが存在しない場合、404エラーになる",
                    example = "sample-api")
            @PathVariable("api") final String api,
            @Parameter(
                    required = true,
                    description = "正常レスポンスのステータスコード<br>"
                            + "・整数以外の場合、400エラーになる<br>"
                            + "・スタブデータが存在しない場合、404エラーになる",
                    example = "200")
            @PathVariable("successCode") final String successCode,
            @Parameter(
                    required = true,
                    description = "異常レスポンスのステータスコード<br>"
                            + "・整数以外の場合、400エラーになる<br>"
                            + "・スタブデータが存在しない場合、404エラーになる",
                    example = "500")
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
