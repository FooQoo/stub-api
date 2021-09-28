package com.fooqoo56.dev.stub.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;

@ConstructorBinding
@ConfigurationProperties(prefix = "stub.enable")
@RequiredArgsConstructor
public class StubConfig {

    @NonNull
    private final Boolean down;

    @NonNull
    private final Boolean timeout;

    /**
     * サーバダウンモードかどうか判定する
     *
     * @return サーバダウンモードの場合、trueを返す
     */
    public boolean isDownMode() {
        return BooleanUtils.isTrue(down);
    }

    /**
     * タイムアウトモードかどうか判定する
     *
     * @return タイムアウトの場合、trueを返す
     */
    public boolean isTimeoutMode() {
        return BooleanUtils.isTrue(timeout);
    }
}
