package com.fooqoo56.dev.stub.domain.helper

import spock.lang.Specification
import spock.lang.Unroll

class SleepHelperSpec extends Specification {

    private SleepHelper sut

    final setup() {
        sut = new SleepHelper()
    }

    @Unroll
    final "runメソッド"() {
        when:
        sut.run(100)

        then:
        noExceptionThrown()
    }
}
