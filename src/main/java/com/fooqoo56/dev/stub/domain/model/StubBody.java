package com.fooqoo56.dev.stub.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooqoo56.dev.stub.exception.InvalidJsonFormatException;
import java.io.IOException;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class StubBody implements Serializable {

    private static final long serialVersionUID = 7281153937276565042L;

    @NonNull
    final String jsonString;

    /**
     * ファクトリメソッド
     *
     * @param jsonString 文字列
     * @return StubBodyを返す
     */
    @NonNull
    public static StubBody from(final String jsonString) throws InvalidJsonFormatException {
        if (isJson(jsonString)) {
            return new StubBody(jsonString);
        } else {
            throw new InvalidJsonFormatException("文字列はJSON形式でありません");
        }

    }

    /**
     * 文字列がJSON形式かどうか判定する
     *
     * @param test 文字列
     * @return JSON形式の場合、trueを返す
     */
    private static boolean isJson(final String test) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(test);
        } catch (final IOException e) {
            return false;
        }
        return true;
    }

    /**
     * toString
     *
     * @return json文字列
     */
    @NonNull
    public String toString() {
        return jsonString;
    }
}
