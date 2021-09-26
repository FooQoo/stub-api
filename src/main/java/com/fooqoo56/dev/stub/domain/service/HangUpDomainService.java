package com.fooqoo56.dev.stub.domain.service;

import com.fooqoo56.dev.stub.exception.FailedSleepException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HangUpDomainService {

    /**
     * スリープ実行
     *
     * @throws FailedSleepException スリープに失敗した場合の例外
     */
    public void sleep(final long duration) throws FailedSleepException {
        try {
            Thread.sleep(duration);
        } catch (final InterruptedException interruptedException) {
            throw new FailedSleepException("スリープに失敗しました:" + interruptedException);
        }
    }
}
