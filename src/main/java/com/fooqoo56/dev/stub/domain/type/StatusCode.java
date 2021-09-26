package com.fooqoo56.dev.stub.domain.type;

import com.fooqoo56.dev.stub.exception.BadRequestException;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StatusCode implements Serializable {

    private static final long serialVersionUID = -4655339988971185938L;

    @NonNull
    final Integer value;

    /**
     * ファクトリメソッド
     *
     * @param value 入力値
     * @return StatusCodeクラスのインスタンス
     * @throws NumberFormatException 数値に変換できない場合
     */
    @NonNull
    private static StatusCode from(final String value) throws NumberFormatException {
        return new StatusCode(Integer.parseInt(value));
    }

    /**
     * 正常ステータスコードの取得
     *
     * @param successCode 正常ステータスコードの文字列
     * @return StatusCodeクラスのインスタンス
     * @throws BadRequestException 入力値が異常な場合
     */
    @NonNull
    public static StatusCode successCodeOf(final String successCode) throws BadRequestException {
        try {
            return StatusCode.from(successCode);
        } catch (final NumberFormatException numberFormatException) {
            throw new BadRequestException(
                    String.format("正常ステータスコードのフォーマットが異常です: %s", successCode));
        }
    }

    /**
     * 異常ステータスコードの取得
     *
     * @param errorCode 正常ステータスコードの文字列
     * @return StatusCodeクラスのインスタンス
     * @throws BadRequestException 入力値が異常な場合
     */
    @NonNull
    public static StatusCode errorCodeOf(final String errorCode) throws BadRequestException {
        try {
            return StatusCode.from(errorCode);
        } catch (final NumberFormatException numberFormatException) {
            throw new BadRequestException(
                    String.format("異常ステータスコードのフォーマットが異常です: %s", errorCode));
        }
    }

    /**
     * 文字列化
     *
     * @return value
     */
    public String toString() {
        return value.toString();
    }
}
