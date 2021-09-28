package com.fooqoo56.dev.stub.infrastructure.api.config;

import java.io.Serializable;
import java.time.Duration;
import lombok.Data;

@Data
public class ApiSetting implements Serializable {

    private static final long serialVersionUID = 4903957119746884341L;

    /**
     * URL
     */
    private String baseUrl;

    /**
     * 接続タイムアウト
     */
    private Duration connectTimeout;

    /**
     * 読み込みタイムアウト
     */
    private Duration readTimeout;

    /**
     * 最大メモリサイズ
     */
    private Integer maxInMemorySize;
}
