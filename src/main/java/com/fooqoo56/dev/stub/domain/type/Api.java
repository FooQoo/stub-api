package com.fooqoo56.dev.stub.domain.type;

import com.fooqoo56.dev.stub.domain.constant.StubApiSetting;
import com.fooqoo56.dev.stub.exception.BadRequestException;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Api implements Serializable {

    private static final long serialVersionUID = -7008643180599915007L;

    @NonNull
    final String value;

    /**
     * ファクトリメソッド
     *
     * @param value 入力値
     * @return Apiクラスのインスタンス
     * @throws BadRequestException apiのフォーマットが異常な場合
     */
    @NonNull
    public static Api from(final String value) throws BadRequestException {
        if (StubApiSetting.isStubApiSetting(value)) {
            return new Api(value);
        } else {
            throw new BadRequestException("apiのフォーマットが異常です");
        }
    }

    /**
     * 文字列化
     *
     * @return value
     */
    public String toString() {
        return value;
    }
}
