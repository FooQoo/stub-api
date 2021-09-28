package com.fooqoo56.dev.stub.presentation.advice

import com.fooqoo56.dev.stub.application.service.StubService
import com.fooqoo56.dev.stub.config.StubConfig
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting
import com.fooqoo56.dev.stub.domain.model.StubBody
import com.fooqoo56.dev.stub.domain.model.StubParam
import com.fooqoo56.dev.stub.domain.service.ResponseConfigDomainService
import com.fooqoo56.dev.stub.exception.FailedSleepException
import com.fooqoo56.dev.stub.exception.NotFoundException
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
import spock.lang.Unroll

@WebFluxTest
@ActiveProfiles("test")
class RestExceptionHandlerE2ESpec extends Specification {

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
        // ResponseConfigDomainServiceのモック
        Mockito.when(responseConfigDomainService.getMediaTypeString(Mockito.any(StubApiSetting)))
                .thenReturn(MediaType.APPLICATION_JSON.toString())
        Mockito.when(responseConfigDomainService.getRawStatusCode(Mockito.any(StubParam)))
                .thenReturn(HttpStatus.OK.value())
    }

    final "バリデーションエラー #caseName"() {
        given:
        final expectedContentType = MediaType.APPLICATION_PROBLEM_JSON
        final expectedStatus = HttpStatus.BAD_REQUEST

        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(false)

        // StubServiceのモック
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))


        when:
        final response = client.get().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/{api}/{successStatus}/{errorStatus}/").build(api, successStatus, errorStatus)
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo("{\"title\":\"Bad Request.\",\"detail\":\"" + expectedBodyDetail + "\"}")
            expectHeader().contentType(expectedContentType)
        }

        where:
        caseName        | api          | successStatus | errorStatus || expectedBodyDetail
        "api"           | "dummy"      | "200"         | "500"       || "[Stub Error]apiのフォーマットが異常です"
        "successStatus" | "sample-api" | "dummy"       | "500"       || "[Stub Error]正常ステータスコードのフォーマットが異常です: dummy"
        "errorStatus"   | "sample-api" | "200"         | "dummy"     || "[Stub Error]異常ステータスコードのフォーマットが異常です: dummy"
    }

    final "404エラー(スタブデータが存在しない) #caseName"() {
        given:
        final api = "sample-api"
        final successStatus = "200"
        final errorStatus = "500"

        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(false)

        final expectedContentType = MediaType.APPLICATION_PROBLEM_JSON
        final expectedStatus = HttpStatus.NOT_FOUND
        final expectedBodyDetail = "[Stub Error]存在しないスタブデータです。"

        // StubServiceのモック
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.error(new NotFoundException("存在しないスタブデータです。")))

        when:
        final response = client.get().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/{api}/{successStatus}/{errorStatus}/").build(api, successStatus, errorStatus)
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo("{\"title\":\"Not Found.\",\"detail\":\"" + expectedBodyDetail + "\"}")
            expectHeader().contentType(expectedContentType)
        }
    }

    @Unroll
    final "内部エラー #caseName"() {
        given:
        final api = "sample-api"
        final successStatus = "200"
        final errorStatus = "500"

        final expectedContentType = MediaType.APPLICATION_PROBLEM_JSON
        final expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR
        final expectedBodyDetail = "[Stub Error]内部エラーが発生しました。"

        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(true)

        // StubServiceのモック
        Mockito.when(stubService.hangUpStubBody(Mockito.any(StubParam)))
                .thenReturn(Mono.error(exception))

        when:
        final response = client.get().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/{api}/{successStatus}/{errorStatus}/").build(api, successStatus, errorStatus)
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo("{\"title\":\"Internal Server Error.\",\"detail\":\"" + expectedBodyDetail + "\"}")
            expectHeader().contentType(expectedContentType)
        }

        where:
        caseName                   | exception
        "IllegalArgumentException" | new IllegalArgumentException()
        "FailedSleepException"     | new FailedSleepException("message")
    }
}
