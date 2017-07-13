package com.epam.testsystem.github.service.webhook;

/**
 * github_test
 * Create on 7/10/2017.
 */

public interface WebhookParserService {
    boolean parse(String payload);
}
