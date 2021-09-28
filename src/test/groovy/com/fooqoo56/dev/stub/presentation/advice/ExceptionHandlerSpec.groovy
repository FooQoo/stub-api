package com.fooqoo56.dev.stub.presentation.advice

import com.fooqoo56.dev.stub.presentation.dto.response.StubDtoErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification
import spock.lang.Unroll

class ExceptionHandlerSpec extends Specification {

    @Unroll
    final "getErrorResponseメソッド #caseName"() {
        when:
        final pair = ExceptionHandler.getErrorResponse(exception)

        then:
        pair.getKey() == status
        pair.getValue() == errorResponse

        where:
        caseName                    | exception                                         || status                           | errorResponse
        "MethodNotAllowedException" | new MethodNotAllowedException("POST", null)       || HttpStatus.METHOD_NOT_ALLOWED    | new StubDtoErrorResponse("Method Not Allowed.", "[Stub Error]許可されないメソッドでアクセスされました")
        "ResponseStatusException"   | new ResponseStatusException(HttpStatus.NOT_FOUND) || HttpStatus.NOT_FOUND             | new StubDtoErrorResponse("Not Found.", "[Stub Error]存在しないパスにアクセスされました")
        "そのほかの例外"                   | new RuntimeException()                            || HttpStatus.INTERNAL_SERVER_ERROR | new StubDtoErrorResponse("Internal Server Error.", "[Stub Error]内部エラーが発生しました。")
    }
}
