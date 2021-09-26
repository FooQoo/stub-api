package com.fooqoo56.dev.stub.infrastructure.api.config;

import io.netty.channel.ChannelOption;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * WebClientの設定クラス
 */
@Configuration
@Slf4j
public class WebClientConfig {

    /**
     * connector
     */
    private static final BiFunction<Duration, Duration, ReactorClientHttpConnector> CONNECTOR =
            (connectTimeout, readTimeout) -> new ReactorClientHttpConnector(HttpClient.create()
                    .responseTimeout(readTimeout)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart())
                    .resolver(DefaultAddressResolverGroup.INSTANCE)
                    .compress(true)
            );

    /**
     * strategy
     */
    private static final Function<Integer, ExchangeStrategies> STRATEGY =
            (maxInMemorySize) -> ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs()
                            .maxInMemorySize(maxInMemorySize))
                    .build();

    /**
     * リクエスト情報出力用
     */
    private static final ExchangeFilterFunction WEBCLIENT_REQUEST_LOGGER = ExchangeFilterFunction
            .ofRequestProcessor(
                    clientRequest -> {
                        log.info("{}[{}] {}",
                                clientRequest.logPrefix(), clientRequest.method(),
                                clientRequest.url());
                        if (!clientRequest.headers().isEmpty()) {
                            log.info(
                                    "{}[GET] Request Headers: {{}}", clientRequest.logPrefix(),
                                    clientRequest.headers().keySet().stream()
                                            .filter(key -> !HttpHeaders.COOKIE.equals(key))
                                            .map(key -> key + "="
                                                    + clientRequest.headers().getFirst(key))
                                            .collect(Collectors.joining(","))
                            );
                        }
                        return Mono.just(clientRequest);
                    }
            );

    /**
     * レスポンス情報出力用
     */
    private static final ExchangeFilterFunction WEBCLIENT_RESPONSE_LOGGER = ExchangeFilterFunction
            .ofResponseProcessor(
                    clientResponse -> {
                        log.info("{}[{}] {}",
                                clientResponse.logPrefix(), clientResponse.statusCode().value(),
                                clientResponse.headers().asHttpHeaders());
                        return Mono.just(clientResponse);
                    }
            );

    /**
     * スタブデータの取得エンドポイント
     *
     * @return api設定値
     */
    @Bean
    @ConfigurationProperties(prefix = "api.stub-data")
    @NonNull
    public ApiSetting stubDataSetting() {
        return new ApiSetting();
    }

    /**
     * スタブデータのクライアント
     *
     * @param apiSetting API設定
     * @return WebClient
     */
    @Bean
    @NonNull
    public WebClient stubDataClient(
            @Qualifier(value = "stubDataSetting") final ApiSetting apiSetting) {

        log.info(apiSetting.toString());

        final var connector = CONNECTOR
                .apply(apiSetting.getConnectTimeout(), apiSetting.getReadTimeout());

        return WebClient.builder()
                .baseUrl(apiSetting.getBaseUrl())
                .filter(WEBCLIENT_REQUEST_LOGGER)
                .filter(WEBCLIENT_RESPONSE_LOGGER)
                .exchangeStrategies(STRATEGY.apply(apiSetting.getMaxInMemorySize()))
                .clientConnector(connector)
                .build();
    }

}