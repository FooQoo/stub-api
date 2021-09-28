package com.fooqoo56.dev.stub.presentation.advice

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fooqoo56.dev.stub.application.service.StubService
import com.fooqoo56.dev.stub.config.StubConfig
import com.fooqoo56.dev.stub.domain.constant.StubApiSetting
import com.fooqoo56.dev.stub.domain.model.StubBody
import com.fooqoo56.dev.stub.domain.model.StubParam
import com.fooqoo56.dev.stub.domain.service.ResponseConfigDomainService
import com.fooqoo56.dev.stub.presentation.dto.response.StubDtoErrorResponse
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
class ExceptionHandlerExceptionE2ESpec extends Specification {

    @Autowired
    private WebTestClient client

    @MockBean
    private StubConfig stubConfig

    @MockBean
    private StubService stubService

    @MockBean
    private ResponseConfigDomainService responseConfigDomainService

    @MockBean
    private ObjectMapper objectMapper

    final setup() {
        // StubConfigのモック
        Mockito.when(stubConfig.isDownMode()).thenReturn(false)
        Mockito.when(stubConfig.isTimeoutMode()).thenReturn(false)
        // ResponseConfigDomainServiceのモック
        Mockito.when(responseConfigDomainService.getMediaTypeString(Mockito.any(StubApiSetting)))
                .thenReturn(MediaType.APPLICATION_JSON.toString())
        Mockito.when(responseConfigDomainService.getRawStatusCode(Mockito.any(StubParam)))
                .thenReturn(HttpStatus.OK.value())
        // StubServiceのモック
        Mockito.when(stubService.hangUpStubBody(Mockito.any(StubParam)))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))
        Mockito.when(stubService.getStubBody(Mockito.any(StubParam), Mockito.anyBoolean()))
                .thenReturn(Mono.just(StubBody.from("{\"key\":\"normal\"}")))
    }

    final "JsonProcessingException発生"() {
        given:
        // ObjectMapperのモック
        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any(StubDtoErrorResponse)))
                .thenThrow(new JsonProcessingException("message"))

        final expectedContentType = MediaType.APPLICATION_JSON
        final expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR

        when:
        final response = client.patch().uri({ uriBuilder ->
            {
                uriBuilder.path("/stub/api/v1/not/found").build()
            }
        }).exchange()

        then:
        verifyAll(response) {
            expectStatus().isEqualTo(expectedStatus)
            expectBody(String.class).isEqualTo(null)
            expectHeader().contentType(expectedContentType)
        }
    }
}
