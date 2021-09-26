package com.fooqoo56.dev.stub.domain.constant

import spock.lang.Specification
import spock.lang.Unroll

class StubApiSettingSpec extends Specification {

    @Unroll
    final "getStubApiSettingメソッド #caseName"() {
        expect:
        StubApiSetting.getStubApiSetting(apiKey) == expected

        where:
        caseName         | apiKey       || expected
        "sample-apiのテスト" | "sample-api" || StubApiSetting.SAMPLE_API
    }

    @Unroll
    final "getStubApiSettingメソッド 存在しないapiKey"() {
        when:
        StubApiSetting.getStubApiSetting("dummy")

        then:
        thrown(IllegalArgumentException)
    }

    @Unroll
    final "isStubApiSettingメソッド #caseName"() {
        expect:
        StubApiSetting.isStubApiSetting(apiKey) == expected

        where:
        caseName      | apiKey       || expected
        "存在するapiKey"  | "sample-api" || true
        "存在しないapiKey" | "dummy"      || false
    }

}
