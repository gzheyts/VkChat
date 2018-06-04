package ru.ssnd.demo.vkchat.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ssnd.demo.vkchat.entity.Message;
import ru.ssnd.demo.vkchat.entity.Sender;
import ru.ssnd.demo.vkchat.repository.MessagesRepository;

import java.util.*;

@Service
public class ChatService {

    private Integer ts = null, newpts = null;

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final Environment environment;

    private final MessagesRepository messagesRepository;

    private final VkApiClient vk;

    private GroupActor groupActor;

    @Autowired
    public ChatService(MessagesRepository messages, Environment environment) {
        this.messagesRepository = messages;
        this.vk = new VkApiClient(new HttpTransportClient());
        this.environment = environment;
        this.groupActor = new GroupActor(Integer.valueOf(environment.getProperty("messages.groupId"))
                , environment.getProperty("messages.group_token"));


    }

    public Long getCommunityId() {
        return Long.valueOf(environment.getProperty("messages.groupId"));
    }


    public void sendMessage(int userId, String message) {
        try {
            vk.messages().send(groupActor).message(message).userId(userId).randomId(new Random().nextInt()).execute();
        } catch (ApiException e) {
            logger.error("INVALID REQUEST", e);
        } catch (ClientException e) {
            logger.error("NETWORK ERROR", e);
        }
    }

    public List<Message> getMessages(Integer userId) throws ClientException, ApiException {

        if (ts == null) {
            ts = vk.messages()
                    .getLongPollServer(groupActor)
                    .execute().getTs();
        }
        MessagesGetLongPollHistoryQuery query = vk.messages().getLongPollHistory(groupActor)
                .ts(ts);

        ClientResponse response = (newpts == null) ? query.executeAsRaw() : query.pts(newpts).executeAsRaw();

        JsonObject root = new JsonParser().parse(response.getContent())
                .getAsJsonObject().get("response").getAsJsonObject();

        newpts = root.get("new_pts").getAsInt();

        if (root.get("messages").getAsJsonObject().get("count").getAsInt() > 0) { // * new messages * !

            JsonObject messages = root.get("messages").getAsJsonObject();

            Map<Integer, JsonObject> msgProfiles = new HashMap<>();

            root.get("profiles").getAsJsonArray()
                    .forEach(o -> msgProfiles.putIfAbsent(o.getAsJsonObject().get("id").getAsInt(), o.getAsJsonObject()));

            List<Message> newMessages = new ArrayList<>(messages.get("count").getAsInt());
            for (JsonElement message : messages.get("items").getAsJsonArray()) {
                JsonObject messageObj = message.getAsJsonObject();
                int uid = messageObj.get("user_id").getAsInt();
                newMessages.add(Message.builder()
                        .id(messageObj.get("id").getAsInt())
                        .sentAt(new Date(messageObj.get("date").getAsLong()))
                        .text(messageObj.get("body").getAsString())
                        .sender(Sender.builder()
                                .id(uid)
                                .name(String.format("%s %s"
                                        , msgProfiles.get(uid).get("first_name").getAsString()
                                        , msgProfiles.get(uid).get("last_name").getAsString())
                                )
                                .avatarUrl(msgProfiles.get(uid).get("photo").getAsString())
                                .build()
                        )
                        .build()
                );
            }
            messagesRepository.save(newMessages);
        }

        return messagesRepository.findBySenderIdOrderBySentAtDesc(userId);
    }
}
