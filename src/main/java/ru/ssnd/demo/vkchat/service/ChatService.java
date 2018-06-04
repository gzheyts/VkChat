package ru.ssnd.demo.vkchat.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssnd.demo.vkchat.repository.MessagesRepository;

@Service
public class ChatService {

    private final MessagesRepository messages;
    private final VkApiClient vk;

    @Autowired
    public ChatService(MessagesRepository messages) {
        this.messages = messages;
        this.vk = new VkApiClient(new HttpTransportClient());
        String communityAccessToken = "You can hardcode your community token here.";
        // TODO Community vk auth
    }

    public Long getCommunityId() {
        // TODO Get community id
        return 0L;
    }

    // TODO Get, send & store messages
}
