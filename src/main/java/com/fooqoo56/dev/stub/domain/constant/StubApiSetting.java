package com.fooqoo56.dev.stub.domain.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Getter
public enum StubApiSetting {
    SAMPLE_API("sample-api", 12000L, MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_PROBLEM_JSON);

    @NonNull
    private final String apiKey;

    @NonNull
    private final Long sleepDuration;

    @NonNull
    private final MediaType successMediaType;

    @NonNull
    private final MediaType errorMediaType;

    /**
     * StubApiSettingを取得する
     *
     * @param apiKey apiKey
     * @return StubApiSetting
     * @throws IllegalArgumentException 存在しないapiKeyの場合の例外
     */
    @NonNull
    public static StubApiSetting getStubApiSetting(final String apiKey)
            throws IllegalArgumentException {
        return Arrays.stream(StubApiSetting.values())
                .filter(stubApiSetting -> stubApiSetting.getApiKey().equals(apiKey))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    /**
     * StubApiSettingが存在するかどうかを判定する
     *
     * @param apiKey apiKey
     * @return apiKeyが存在する場合は、trueを返す
     */
    @NonNull
    public static boolean isStubApiSetting(final String apiKey)
            throws IllegalArgumentException {
        return Arrays.stream(StubApiSetting.values())
                .anyMatch(stubApiSetting -> stubApiSetting.getApiKey().equals(apiKey));
    }
}
