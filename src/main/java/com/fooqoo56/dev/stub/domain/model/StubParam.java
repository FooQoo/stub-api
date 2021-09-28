package com.fooqoo56.dev.stub.domain.model;

import com.fooqoo56.dev.stub.domain.type.Api;
import com.fooqoo56.dev.stub.domain.type.StatusCode;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public class StubParam implements Serializable {

    private static final long serialVersionUID = -6403246589132227245L;

    @NonNull
    final Api api;

    @NonNull
    final StatusCode successCode;

    @NonNull
    final StatusCode errorCode;

    /**
     * ファクトリメソッド
     *
     * @param api         api文字列
     * @param successCode 正常系ステータスコード
     * @param errorCode   異常系ステータスコード
     * @return StubPathParamインスタンス
     */
    @NonNull
    public static StubParam of(final String api, final String successCode,
                               final String errorCode) {

        return StubParam.builder()
                .api(Api.from(api))
                .successCode(StatusCode.successCodeOf(successCode))
                .errorCode(StatusCode.errorCodeOf(errorCode))
                .build();
    }
}
