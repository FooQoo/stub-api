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

        exchange.getResponse().setRawStatusCode(getRawStatusCode(stubParam));
        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.CONTENT_TYPE, getMediaTypeString(stubApiSetting));
    }

    /**
     * MediaTypeを文字列で取得する
     *
     * @param stubApiSetting スタブAPIの設定
     * @return MediaTypeの文字列
     */
    private String getMediaTypeString(final StubApiSetting stubApiSetting) {
        final var mediaType = stubConfig.isDownMode() ? stubApiSetting.getErrorMediaType() :
                stubApiSetting.getSuccessMediaType();

        return mediaType.toString();
    }

    /**
     * ステータスコードを数値で取得する
     *
     * @param stubParam スタブパラメータ
     * @return ステータスコードを数値
     */
    private Integer getRawStatusCode(final StubParam stubParam) {
        final var statusCode =
                stubConfig.isDownMode() ? stubParam.getErrorCode() : stubParam.getSuccessCode();

        return statusCode.getValue();
    }
}
