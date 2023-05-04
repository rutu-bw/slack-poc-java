package com.TestPoc.TestPoc.Config;

import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.SlackClientFactory;
import com.hubspot.slack.client.SlackClientRuntimeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SlackConfig {

    private final Environment env;

    public SlackConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SlackClient slackClient(){
        SlackClientRuntimeConfig runtimeConfig = SlackClientRuntimeConfig.builder()
                .setTokenSupplier(() -> env.getRequiredProperty("SLACK_BOT_TOKEN_MAIN"))
                .build();

        return SlackClientFactory.defaultFactory().build(runtimeConfig);
    }

}
