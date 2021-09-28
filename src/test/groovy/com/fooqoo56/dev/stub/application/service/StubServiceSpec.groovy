package com.fooqoo56.dev.stub.application.service

import com.fooqoo56.dev.stub.domain.model.StubBody
import com.fooqoo56.dev.stub.domain.model.StubParam
import com.fooqoo56.dev.stub.domain.repository.api.StubDataRepository
import com.fooqoo56.dev.stub.domain.service.HangUpDomainService
import com.fooqoo56.dev.stub.domain.type.Api
import com.fooqoo56.dev.stub.domain.type.StatusCode
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

class StubServiceSpec extends Specification {

    private StubService sut
    private StubDataRepository stubDataRepository
    private HangUpDomainService hangUpDomainService

    final setup() {
        hangUpDomainService = Mock(HangUpDomainService) {
            sleep(*_) >> {
                return
            }
        }
        stubDataRepository = Mock(StubDataRepository) {
            getSuccessStubData(*_) >> Mono.just(new StubBody("{\"key\":\"successBody\"}"))
            getErrorStubData(*_) >> Mono.just(new StubBody("{\"key\":\"errorBody\"}"))
        }
        sut = new StubService(stubDataRepository, hangUpDomainService)
    }

    @Unroll
    final "getStubBodyメソッド #caseName"() {
        expect:
        sut.getStubBody(Mock(StubParam), isDownMode).block() == new StubBody(expected)

        where:
        caseName    | isDownMode || expected
        "通常モード"     | false      || "{\"key\":\"successBody\"}"
        "サーバダウンモード" | true       || "{\"key\":\"errorBody\"}"
    }

    @Unroll
    final "hangUpStubBodyメソッド"() {
        given:
        final stubParam = new StubParam(
                new Api("sample-api"),
                new StatusCode(200),
                new StatusCode(500))
        expect:
        sut.hangUpStubBody(stubParam).block() == new StubBody("{\"key\":\"successBody\"}")
    }
}
