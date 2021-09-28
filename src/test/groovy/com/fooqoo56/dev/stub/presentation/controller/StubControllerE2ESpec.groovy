package com.fooqoo56.dev.stub.presentation.controller

import com.fooqoo56.dev.stub.application.service.StubService
import com.fooqoo56.dev.stub.config.StubConfig
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting
import com.fooqoo56.dev.stub.domain.model.StubBody
import com.fooqoo56.dev.stub.domain.model.StubParam
import com.fooqoo56.dev.stub.domain.service.ResponseConfigDomainService
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

@WebFluxTest
@ActiveProfiles("test")
class StubControllerE2ESpec extends Specification {

    @Autowired
    private WebTestClient client

    @MockBean
    private StubConfig stubConfig

    @MockBean
    private StubService stubService

    @MockBean
    private ResponseConfigDomainService responseConfigDomainService

    final setup() {
        Mockito.when(stubConfig.isDownMode()).thenReturn(false)
        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(false)
    }

    final "GETメソッドでスタブを返却する #caseName"() {
        given:
        final api = "sample-api"
        final successStatus = "200"
        final errorStatus = "500"

        // StubConfigのモック
        Mockito.when(stubConfig.isDownMode()).thenReturn(isDownMode)
        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(isTimeoutMode)
        // StubServiceのモック
        Mockito.when(stubService.hangUpStubBody(Mockito.any(StubParam)))
                .thenReturn(Mono.just(StubBody.from(expectedBody)))
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.just(StubBody.from(expectedBody)))
        // ResponseConfigDomainServiceのモック
        Mockito.when(responseConfigDomainService.getMediaTypeString(Mockito.any(StubApiSetting)))
                .thenReturn(expectedContentType.toString())
        Mockito.when(responseConfigDomainService.getRawStatusCode(Mockito.any(StubParam)))
                .thenReturn(expectedStatus.value())

        when:
        final response = client.get().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/{api}/{successStatus}/{errorStatus}/").build(api, successStatus, errorStatus)
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo(expectedBody)
            expectHeader().contentType(expectedContentType)
        }

        where:
        caseName | isDownMode | isTimeoutMode || expectedStatus                   | expectedBody            | expectedContentType
        "通常"     | false      | false         || HttpStatus.OK                    | "{\"key\":\"normal\"}"  | MediaType.APPLICATION_JSON
        "サーバダウン" | true       | false         || HttpStatus.INTERNAL_SERVER_ERROR | "{\"key\":\"down\"}"    | MediaType.APPLICATION_PROBLEM_JSON
        "タイムアウト" | false      | true          || HttpStatus.OK                    | "{\"key\":\"timeout\"}" | MediaType.APPLICATION_JSON
    }
}