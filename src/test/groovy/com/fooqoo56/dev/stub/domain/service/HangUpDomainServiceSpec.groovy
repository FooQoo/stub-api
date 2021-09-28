package com.fooqoo56.dev.stub.domain.service

import com.fooqoo56.dev.stub.domain.helper.SleepHelper
import com.fooqoo56.dev.stub.exception.FailedSleepException
import spock.lang.Specification
import spock.lang.Unroll

class HangUpDomainServiceSpec extends Specification {

    private SleepHelper sleepHelper

    private HangUpDomainService sut

    final setup() {
        sleepHelper = Mock(SleepHelper)
        sut = new HangUpDomainService(sleepHelper)
    }

    @Unroll
    final "sleepメソッド"() {
        when:
        sut.sleep(100)

        then:
        noExceptionThrown()
    }

    @Unroll
    final "sleepメソッド 例外発生"() {
        given:
        sleepHelper.run(*_) >> { throw new InterruptedException() }

        when:
        sut.sleep(100)

        then:
        final exception = thrown(FailedSleepException)
        exception.getMessage() == "スリープに失敗しました"
    }
}
