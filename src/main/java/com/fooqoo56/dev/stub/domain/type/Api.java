package com.fooqoo56.dev.stub.domain.type;

import com.fooqoo56.dev.stub.exception.BadRequestException;
import java.io.Serializable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Api implements Serializable {

    private static final long serialVersionUID = -7008643180599915007L;

    private static final Pattern pattern = Pattern.compile("[a-z\\-]");

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
        if (pattern.matcher(value).find()) {
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
