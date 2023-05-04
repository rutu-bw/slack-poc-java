package com.TestPoc.TestPoc.Controller;

import com.TestPoc.TestPoc.Model.SlackUserRecipient;
import com.TestPoc.TestPoc.Service.SlackSendingService;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messaging")
public class MessagingController {

    private final SlackSendingService slackSendingService;
    private final Environment env;

    @PostMapping("/slack/channel")
    public void sendSlackMessage(){
        String message = "Hello, Rutu Here.\nThis is test message.";
        slackSendingService.sendMessage(message);
    }

    @PostMapping("/slack/dm")
    public ChatPostMessageResponse sendSlackMessageDM(){

        SlackUserRecipient userRecipient = new SlackUserRecipient(env.getProperty("SLACK_WORKSPACE"),env.getProperty("SLACK_COLIN"));
        String message = "Hello, Rutu Here.\nThis is test message.";
        ChatPostMessageResponse response = slackSendingService.sendMessageDm(userRecipient,"Colin",message,"This is System Generated Message. Do not reply.");
        return response;
    }




}
