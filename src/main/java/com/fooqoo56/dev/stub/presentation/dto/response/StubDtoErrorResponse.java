package com.fooqoo56.dev.stub.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@Schema(description = "エラーレスポンス")
public class StubDtoErrorResponse implements Serializable {

    private static final long serialVersionUID = 3316176567352799499L;

    @Schema(type = "string", required = true, description = "ステータスコードのタイトル")
    @JsonProperty
    private String title;

    @Schema(type = "string", required = true, description = "エラー詳細")
    @JsonProperty
    private String detail;

    /**
     * Internal Server Error用レスポンスを取得する
     *
     * @return エラーレスポンス
     */
    public static StubDtoErrorResponse getInternalServerError() {
        return StubDtoErrorResponse.builder()
                .title("Internal Server Error.")
                .detail("[Stub Error]内部エラーが発生しました。")
                .build();
    }

    /**
     * Bad Request用レスポンスを取得する
     *
     * @return エラーレスポンス
     */
    public static StubDtoErrorResponse getBadRequest(final String message) {
        return StubDtoErrorResponse.builder()
                .title("Bad Request.")
                .detail("[Stub Error]" + message)
                .build();
    }

    /**
     * Not Found用レスポンスを取得する
     *
     * @return エラーレスポンス
     */
    public static StubDtoErrorResponse getNotFound(final String message) {
        return StubDtoErrorResponse.builder()
                .title("Not Found.")
                .detail("[Stub Error]" + message)
                .build();
    }

    /**
     * Method Not Allowed用レスポンスを取得する
     *
     * @return エラーレスポンス
     */
    public static StubDtoErrorResponse getMethodNotAllowed() {
        return StubDtoErrorResponse.builder()
                .title("Method Not Allowed.")
                .detail("[Stub Error]許可されないメソッドでアクセスされました")
                .build();
    }
}
