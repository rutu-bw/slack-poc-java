package com.TestPoc.TestPoc.Service;

import com.TestPoc.TestPoc.Model.SlackUserRecipient;
import com.hubspot.slack.client.SlackClient;
import com.hubspot.slack.client.methods.params.chat.ChatPostMessageParams;
import com.hubspot.slack.client.methods.params.conversations.ConversationOpenParams;
import com.hubspot.slack.client.models.blocks.Section;
import com.hubspot.slack.client.models.blocks.objects.Text;
import com.hubspot.slack.client.models.blocks.objects.TextType;
import com.hubspot.slack.client.models.response.chat.ChatPostMessageResponse;
import com.hubspot.slack.client.models.response.conversations.ConversationsOpenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SlackSendingService {

    private final SlackClient slackClient;
    private final Environment env;

    public SlackSendingService(SlackClient slackClient, Environment env) {
        this.slackClient = slackClient;
        this.env = env;
    }

    public ChatPostMessageResponse sendMessageDm(SlackUserRecipient recipient, String name, String message, String note) {
        ChatPostMessageResponse response = null;
        try {
            // Open Conversation
            ConversationsOpenResponse conOpenResponse = openConversation(recipient.getUserId());

            // Send Message
            response = sendMessage(buildMessageForReminder(conOpenResponse.getConversation().getId(), name, message, note));
        } catch (IllegalStateException ex) {
            log.warn("Unable to Send Private Message", ex);
        }
        return response;
    }

    public ChatPostMessageParams buildMessageForReminder(String userId, String name, String message, String note) {
        List<Section> sectionList = new ArrayList<>();
        Section titleSection = Section.builder()
                .setText(Text.builder()
                        .setType(TextType.MARKDOWN)
                        .setText("*Hey " + name + ",*")
                        .build())
                .build();
        sectionList.add(titleSection);

        Section messageSection = Section.builder()
                .setText(Text.builder()
                        .setType(TextType.MARKDOWN)
                        .setText(message)
                        .build())
                .build();
        sectionList.add(messageSection);

        Section noteSection = Section.builder()
                .setText(Text.builder()
                        .setType(TextType.MARKDOWN)
                        .setText("*Note:* " + note)
                        .build())
                .build();
        if (StringUtils.hasText(note))
            sectionList.add(noteSection);

        Section regardsSection = Section.builder()
                .setText(Text.builder()
                        .setType(TextType.MARKDOWN)
                        .setText("*Regards,* \nDemo Reliant")
                        .build())
                .build();
        sectionList.add(regardsSection);

        return ChatPostMessageParams.builder()
                .setText("Test Direct Message")
                .setChannelId(userId)
                .setBlocks(sectionList).build();
    }

    public ConversationsOpenResponse openConversation(String userId){
        return slackClient.openConversation(ConversationOpenParams.builder()
                .setUsers(Collections.singletonList(userId)).build()
        ).join().unwrapOrElseThrow();
    }

    public void sendMessage(String message){
        this.sendMessage(this.buildBugReportMessage(env.getRequiredProperty("SLACK_FEATURE_CHANNEL"), message));
    }

    public ChatPostMessageParams buildBugReportMessage(final String channel, final String message){
        return ChatPostMessageParams.builder()
                .setChannelId(channel)
                .setText(message)
                .build();

    }

    public ChatPostMessageResponse sendMessage(ChatPostMessageParams params){
        try{
            // Send Message
            return slackClient.postMessage(params).join().unwrapOrElseThrow();
        }catch (IllegalStateException ex){
            log.warn("Unable to Send Message", ex);
            return null;
        }
    }
}
