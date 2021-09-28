package com.fooqoo56.dev.stub.domain.service;

import com.fooqoo56.dev.stub.config.StubConfig;
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.domain.model.StubParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseConfigDomainService {

    private final StubConfig stubConfig;

    /**
     * MediaTypeを文字列で取得する
     *
     * @param stubApiSetting スタブAPIの設定
     * @return MediaTypeの文字列
     */
    public String getMediaTypeString(final StubApiSetting stubApiSetting) {
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
    public Integer getRawStatusCode(final StubParam stubParam) {
        final var statusCode =
                stubConfig.isDownMode() ? stubParam.getErrorCode() : stubParam.getSuccessCode();

        return statusCode.getValue();
    }
}
