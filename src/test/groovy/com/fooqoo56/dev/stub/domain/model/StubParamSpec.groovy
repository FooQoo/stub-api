package com.fooqoo56.dev.stub.domain.model

import com.fooqoo56.dev.stub.domain.type.Api
import com.fooqoo56.dev.stub.domain.type.StatusCode
import spock.lang.Specification
import spock.lang.Unroll

class StubParamSpec extends Specification {

    @Unroll
    final "ファクトリメソッド"() {
        given:
        final expected = new StubParam(new Api("sample-api"), new StatusCode(200), new StatusCode(500))
        expect:
        StubParam.of("sample-api", "200", "500") == expected
    }
}
