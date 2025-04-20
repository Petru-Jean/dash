package org.dash.event.incoming;

import org.dash.entity.MessageCreateEventEntity;
import org.dash.event.annotations.GatewayEvent;
import org.dash.model.EventPayload;
import org.dash.repository.MessageRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@GatewayEvent(type = IncomingEvents.MESSAGE_CREATE)
public class MessageCreateEvent implements IncomingEvent
{

    MessageRepository repository;

    @Autowired
    public MessageCreateEvent(MessageRepository messageRepository)
    {
        this.repository = messageRepository;
    }

    @Override
    public void process(EventPayload payload)
    {
        try
        {
            JSONObject data = (JSONObject) payload.getData();
            
            JSONObject author     = data.getJSONObject("author");
            String     messageId  = data.getString("id");

            if(author.has("webhook_id") || !data.has("member")) {
                System.out.println("[MessageCreateEvent] Message " + messageId + " not processed as it was either ephemeral or from webhooks");
                return;
            }

            String userId = author.getString("id");
            String content = data.getString("content");
            String channelId = data.getString("channel_id");
            ZonedDateTime timestamp = ZonedDateTime.parse(data.getString("timestamp"));

            JSONObject member = data.getJSONObject("member");
            String nickname = member.optString("nick", null);
            String username = author.getString("username");

            var entity = new MessageCreateEventEntity();
            entity.setMessageId(messageId);
            entity.setUserId(userId);
            entity.setContent(content);
            entity.setChannelId(channelId);
            entity.setNickname(nickname);
            entity.setUsername(username);
            entity.setTimestamp(timestamp);

            System.out.println("message create " + content);

            repository.save(entity);
        }
        catch(JSONException ex)
        {
            System.out.println("[Message Create Event] Exception when processing event " + ex.getMessage() + " " + ex);
        }
        
    }


}
