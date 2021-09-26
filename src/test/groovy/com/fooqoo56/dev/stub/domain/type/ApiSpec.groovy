package com.fooqoo56.dev.stub.domain.type

import com.fooqoo56.dev.stub.exception.BadRequestException
import spock.lang.Specification

class ApiSpec extends Specification {

    final "ファクトリメソッド"() {
        expect:
        Api.from("sample-api") == new Api("sample-api")
    }

    final "ファクトリメソッド 存在しないapi"() {
        when:
        Api.from("dummy")

        then:
        final exception = thrown(BadRequestException)
        exception.getMessage() == "apiのフォーマットが異常です"
    }

    final "toStringメソッド"() {
        given:
        final api = Api.from("sample-api")

        expect:
        api.toString() == "sample-api"
    }
}
