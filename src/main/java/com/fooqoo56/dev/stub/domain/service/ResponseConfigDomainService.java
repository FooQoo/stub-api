package com.fooqoo56.dev.stub.domain.service;

import com.fooqoo56.dev.stub.config.StubConfig;
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

@Service
@RequiredArgsConstructor
public class ResponseConfigDomainService {

    private final StubConfig stubConfig;

    /**
     * Response情報をセットする
     *
     * @param stubParam スタブのパラメータ
     * @param exchange  ServerWebExchange
     */
    public void setResponse(final StubParam stubParam, final ServerWebExchange exchange) {
        final var stubApiSetting = StubApiSetting.getStubApiSetting(stubParam.getApi().toString());

        final var mediaType = stubConfig.isDownMode() ? stubApiSetting.getErrorMediaType() :
                stubApiSetting.getSuccessMediaType();

        final var statusCode =
                stubConfig.isDownMode() ? stubParam.getErrorCode() : stubParam.getSuccessCode();

        exchange.getResponse().setRawStatusCode(statusCode.getValue());
        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, mediaType.toString());
    }
}
