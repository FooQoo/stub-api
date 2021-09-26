package com.fooqoo56.dev.stub.domain.service

import com.fooqoo56.dev.stub.config.StubConfig
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting
import com.fooqoo56.dev.stub.domain.model.StubParam
import org.springframework.http.MediaType
import spock.lang.Specification
import spock.lang.Unroll

class ResponseConfigDomainServiceSpec extends Specification {

    private ResponseConfigDomainService sut

    @Unroll
    final "getMediaTypeStringメソッド #caseName"() {
        given:
        sut = new ResponseConfigDomainService(new StubConfig(isDownMode, false))

        expect:
        sut.getMediaTypeString(StubApiSetting.SAMPLE_API) == expected

        where:
        caseName | isDownMode || expected
        "ダウンモード" | true       || MediaType.APPLICATION_PROBLEM_JSON.toString()
        "通常"     | false      || MediaType.APPLICATION_JSON.toString()
    }

    @Unroll
    final "getRawStatusCodeメソッド #caseName"() {
        given:
        sut = new ResponseConfigDomainService(new StubConfig(isDownMode, false))
        final stubParam = StubParam.of("sample-api", "200", "400")

        expect:
        sut.getRawStatusCode(stubParam) == expected

        where:
        caseName | isDownMode || expected
        "ダウンモード" | true       || 400
        "通常"     | false      || 200
    }

}
