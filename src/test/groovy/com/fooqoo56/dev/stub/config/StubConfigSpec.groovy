package com.fooqoo56.dev.stub.config

import spock.lang.Specification
import spock.lang.Unroll

class StubConfigSpec extends Specification {

    @Unroll
    final "isDownModeメソッド #caseName"() {
        given:
        final stubConfig = new StubConfig(down, false)

        expect:
        stubConfig.isDownMode() == expected

        where:
        caseName   | down  || expected
        "trueの場合"  | true  || true
        "falseの場合" | false || false
    }

    @Unroll
    final "isTimeoutModeメソッド #caseName"() {
        given:
        final stubConfig = new StubConfig(true, timeout)

        expect:
        stubConfig.isTimeoutMode() == expected

        where:
        caseName   | timeout || expected
        "trueの場合"  | true    || true
        "falseの場合" | false   || false
    }
}


