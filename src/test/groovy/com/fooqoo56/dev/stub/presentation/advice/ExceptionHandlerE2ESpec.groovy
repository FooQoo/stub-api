package com.fooqoo56.dev.stub.presentation.advice

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
class ExceptionHandlerE2ESpec extends Specification {

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
        // ResponseConfigDomainServiceのモック
        Mockito.when(responseConfigDomainService.getMediaTypeString(Mockito.any(StubApiSetting)))
                .thenReturn(MediaType.APPLICATION_JSON.toString())
        Mockito.when(responseConfigDomainService.getRawStatusCode(Mockito.any(StubParam)))
                .thenReturn(HttpStatus.OK.value())
    }

    final "405"() {
        given:
        final api = "sample-api"
        final successStatus = "200"
        final errorStatus = "500"

        final expectedContentType = MediaType.APPLICATION_PROBLEM_JSON
        final expectedStatus = HttpStatus.METHOD_NOT_ALLOWED
        final expectedBodyDetail = "[Stub Error]許可されないメソッドでアクセスされました"

        // StubServiceのモック
        Mockito.when(stubService.hangUpStubBody(Mockito.any(StubParam)))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))


        when:
        final response = client.patch().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/{api}/{successStatus}/{errorStatus}/").build(api, successStatus, errorStatus)
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo("{\"title\":\"Method Not Allowed.\",\"detail\":\"" + expectedBodyDetail + "\"}")
            expectHeader().contentType(expectedContentType)
        }
    }

    final "404 存在しないエンドポイント"() {
        given:
        final expectedContentType = MediaType.APPLICATION_PROBLEM_JSON
        final expectedStatus = HttpStatus.NOT_FOUND
        final expectedBodyDetail = "[Stub Error]存在しないパスにアクセスされました"

        // StubServiceのモック
        Mockito.when(stubService.hangUpStubBody(Mockito.any(StubParam)))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))


        when:
        final response = client.patch().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/not/found").build()
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo("{\"title\":\"Not Found.\",\"detail\":\"" + expectedBodyDetail + "\"}")
            expectHeader().contentType(expectedContentType)
        }
    }
}
