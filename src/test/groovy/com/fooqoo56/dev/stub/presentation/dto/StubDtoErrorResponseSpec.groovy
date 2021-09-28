package com.fooqoo56.dev.stub.presentation.dto

import com.fooqoo56.dev.stub.presentation.dto.response.StubDtoErrorResponse
import spock.lang.Specification

class StubDtoErrorResponseSpec extends Specification {

    final "getInternalServerError"() {
        expect:
        StubDtoErrorResponse.getInternalServerError() == new StubDtoErrorResponse("Internal Server Error.", "[Stub Error]内部エラーが発生しました。")
    }

    final "getBadRequest"() {
        expect:
        StubDtoErrorResponse.getBadRequest("message") == new StubDtoErrorResponse("Bad Request.", "[Stub Error]message")
    }

    final "getNotFound"() {
        expect:
        StubDtoErrorResponse.getNotFound("message") == new StubDtoErrorResponse("Not Found.", "[Stub Error]message")
    }

    final "getMethodNotAllowed"() {
        expect:
        StubDtoErrorResponse.getMethodNotAllowed() == new StubDtoErrorResponse("Method Not Allowed.", "[Stub Error]許可されないメソッドでアクセスされました")
    }
}
