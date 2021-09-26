package com.fooqoo56.dev.stub.domain.service


import spock.lang.Specification
import spock.lang.Unroll

class HangUpDomainServiceSpec extends Specification {


    private HangUpDomainService sut

    final setup() {
        sut = new HangUpDomainService()
    }

    @Unroll
    final "sleepメソッド"() {
        when:
        sut.sleep(100)

        then:
        noExceptionThrown()
    }
}
