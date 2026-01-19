package com.deukyunlee.indexer;

import com.deukyunlee.indexer.model.account.request.AccountTokenV1Request;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
public class AccountTokenV1RequestTest {

    private final Validator validator = createValidator();

    private Validator createValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        return factory;
    }

    @Test
    public void validRequest_hasNoValidationErrors() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "0x1234567890abcdef1234567890abcdef12345678",
                "0x5a3e6A77ba2f983eC0d371ea3B475F8Bc0811AD5",
                LocalDate.now()
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertFalse(result.hasErrors(), "Valid request should not have validation errors.");
    }

    @Test
    public void invalidAccountAddressWithout0x_hasErrors() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "12345",
                "0x5a3e6A77ba2f983eC0d371ea3B475F8Bc0811AD5",
                LocalDate.now()
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertTrue(result.hasErrors(), "Invalid account address should produce validation errors.");
    }

    @Test
    public void invalidAccountAddressWithShortLength_hasErrors() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "0xabcdefabcdefabcdefabcdefabcdefabcdefabc",
                "0x5a3e6A77ba2f983eC0d371ea3B475F8Bc0811AD5",
                LocalDate.now()
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertTrue(result.hasErrors(), "Invalid account address should produce validation errors.");
    }

    @Test
    public void invalidTokenAddress_hasErrors() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "0x1234567890abcdef1234567890abcdef12345678",
                "INVALID_TOKEN",
                LocalDate.now()
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertTrue(result.hasErrors(), "Invalid token address should produce validation errors.");
    }

    @Test
    public void futureDate_hasErrors() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "0x1234567890abcdef1234567890abcdef12345678",
                "0xabcdefabcdefabcdefabcdefabcdefabcdefabcd",
                LocalDate.now().plusDays(1)
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertTrue(result.hasErrors(), "Future date should produce validation errors.");
    }

    @Test
    public void ethTokenAddress_isAllowed() {
        AccountTokenV1Request request = new AccountTokenV1Request(
                "0x1234567890abcdef1234567890abcdef12345678",
                "ETH",
                LocalDate.now()
        );

        BindingResult result = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, result);

        assertFalse(result.hasErrors(), "ETH as token address should not produce validation errors.");
    }
}
