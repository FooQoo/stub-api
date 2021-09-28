package com.fooqoo56.dev.stub.domain.type

import com.fooqoo56.dev.stub.exception.BadRequestException
import spock.lang.Specification

class StatusCodeSpec extends Specification {

    final "ファクトリメソッド"() {
        expect:
        StatusCode.from("400") == new StatusCode(400)
    }

    final "ファクトリメソッド 数値出ない場合"() {
        when:
        StatusCode.from("hoge")

        then:
        thrown(NumberFormatException)
    }

    final "successCodeOf"() {
        expect:
        StatusCode.successCodeOf("200") == new StatusCode(200)
    }

    final "successCodeOf フォーマット異常"() {
        when:
        StatusCode.successCodeOf("hoge")

        then:
        final exception = thrown(BadRequestException)
        exception.getMessage() == "正常ステータスコードのフォーマットが異常です: hoge"
    }

    final "errorCodeOf"() {
        expect:
        StatusCode.successCodeOf("500") == new StatusCode(500)
    }

    final "errorCodeOf フォーマット異常"() {
        when:
        StatusCode.errorCodeOf("hoge")

        then:
        final exception = thrown(BadRequestException)
        exception.getMessage() == "異常ステータスコードのフォーマットが異常です: hoge"
    }

    final "toStringメソッド"() {
        given:
        final statusCode = StatusCode.from("400")
        expect:
        statusCode.toString() == "400"
    }
}
