package com.fooqoo56.dev.stub.infrastructure.api.repositoryimpl

import com.fooqoo56.dev.stub.domain.model.StubBody
import com.fooqoo56.dev.stub.domain.model.StubParam
import com.fooqoo56.dev.stub.domain.type.Api
import com.fooqoo56.dev.stub.domain.type.StatusCode
import com.fooqoo56.dev.stub.exception.NotFoundException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class StubDataRepositoryImplSpec extends Specification {

    private MockWebServer mockWebServer
    private WebClient webClient
    private StubDataRepositoryImpl sut

    final setup() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
        webClient = WebClient.create(mockWebServer.url("/").toString())
        sut = new StubDataRepositoryImpl(webClient)
    }

    final cleanup() {
        mockWebServer.shutdown()
    }

    final "getSuccessStubDataメソッド"() {
        given:
        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/sample-api/200.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(mockResponse))

        final stubParam = StubParam.of("sample-api", "200", "400")

        final expected = StubBody.from("{\n  \"key\": \"value\"\n}\n")

        when:
        final actual = sut.getSuccessStubData(stubParam).block()

        then:
        actual == expected
    }

    final "getErrorStubDataメソッド"() {
        given:
        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/sample-api/400.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(mockResponse))

        final stubParam = StubParam.of("sample-api", "200", "400")

        final expected = StubBody.from("{\n  \"title\": \"Bad Request\",\n  \"detail\": \"Bad Request Exception\"\n}\n")

        when:
        final actual = sut.getErrorStubData(stubParam).block()

        then:
        actual == expected
    }

    final "getStubDataメソッド #caseName"() {
        given:
        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/sample-api/" + statusCode + ".json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .setBody(mockResponse))

        when:
        final actual = sut.getStubData(new Api("sample-api"), new StatusCode(statusCode)).block()

        then:
        actual == StubBody.from(expected)

        where:
        caseName       | statusCode || expected
        "ステータスコードが400" | 400        || "{\n  \"title\": \"Bad Request\",\n  \"detail\": \"Bad Request Exception\"\n}\n"
        "ステータスコードが200" | 200        || "{\n  \"key\": \"value\"\n}\n"
    }

    final "getStubDataメソッド 存在しないスタブデータの場合"() {
        given:
        // mockサーバを作成する
        final mockResponse = new FileReader("src/test/resources/sample-api/200.json").text
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON)
                        .setBody(mockResponse))

        when:
        sut.getStubData(new Api("sample-api"), new StatusCode(500)).block()

        then:
        final exception = thrown(NotFoundException)
        exception.getMessage() == "存在しないスタブデータです。"
    }
}
