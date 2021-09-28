package com.fooqoo56.dev.stub.domain.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SleepHelper {

    /**
     * sleep実行
     *
     * @param duration スリープ時間[ms]
     * @throws InterruptedException スリープが妨害された場合に発生する例外
     */
    public void run(final long duration) throws InterruptedException {
        Thread.sleep(duration);
    }
}
