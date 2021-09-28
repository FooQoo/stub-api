package com.fooqoo56.dev.stub.domain.model

import com.fooqoo56.dev.stub.exception.InvalidJsonFormatException
import spock.lang.Specification
import spock.lang.Unroll

class StubBodySpec extends Specification {

    @Unroll
    final "ファクトリメソッド"() {
        expect:
        StubBody.from("{\"key\":\"value\"}") == new StubBody("{\"key\":\"value\"}")
    }

    @Unroll
    final "ファクトリメソッド 無効なJSONのフォーマット"() {
        when:
        StubBody.from("hoge)adaf")

        then:
        final exception = thrown(InvalidJsonFormatException)
        exception.getMessage() == "文字列はJSON形式でありません"
    }

    @Unroll
    final "isJsonのメソッド #caseName"() {
        expect:
        StubBody.isJson(json) == expect

        where:
        caseName        | json                  || expect
        "正しいJSONフォーマット" | "{\"key\":\"value\"}" || true
        "異常なJSONフォーマット" | "hoge)adaf"           || false
    }

    @Unroll
    final "toStringメソッド"() {
        given:
        final stubBody = StubBody.from("{\"key\":\"value\"}")

        expect:
        stubBody.toString() == "{\"key\":\"value\"}"
    }
}
