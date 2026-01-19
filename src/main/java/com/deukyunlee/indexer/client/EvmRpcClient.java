package com.deukyunlee.indexer.client;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.spec.RpcSpec;
import com.deukyunlee.indexer.util.SleepUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvmRpcClient implements RpcSpec {
    @Value("${infura.apiKey}")
    private String INFURA_API_KEY;

    private static final int MAX_DELAY = 32000;

    private final RestTemplate restTemplate;

    public <T> T callPost(EvmChainType evmChainType, String jsonInput, int maxRetries, Class<T> classes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonInput, headers);
        int retries = 0;

        int backoffDelay = 1000;
        while (retries < maxRetries) {
            try {
                ResponseEntity<T> response = restTemplate.postForEntity(String.format(evmChainType.getInfuraUrl(), INFURA_API_KEY), request, classes);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                }
            } catch (Exception e) {
                log.error("RPC Error Occurred: {}, {}, \nRetries: {}/{}", jsonInput, e.getMessage(), retries, maxRetries);
                SleepUtil.wait(backoffDelay);

                backoffDelay = Math.min(backoffDelay * 2, MAX_DELAY);
                retries++;
            }
        }
        throw new ErrorTypeException("RPC_CALL_FAILED", CustomErrorType.RPC_CALL_FAILED);
    }
}