package ru.ssnd.demo.vkchat.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.ssnd.demo.vkchat.http.PollResponse;
import ru.ssnd.demo.vkchat.http.Response;
import ru.ssnd.demo.vkchat.service.ChatService;

@RestController
@RequestMapping(value = "/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "{interlocutorId}/poll")
    public DeferredResult<PollResponse> poll(@PathVariable Integer interlocutorId) {

        DeferredResult<PollResponse> result = new DeferredResult<>();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                result.setResult(PollResponse.builder().messages(chatService.getMessages(interlocutorId)).build());
            } catch (ClientException | ApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return result;
    }

    @PostMapping(value = "{interlocutorId}/send")
    public Response send(@PathVariable Integer interlocutorId, @RequestBody String message) {

        chatService.sendMessage(interlocutorId, message);
        return new Response.Builder()
                .withField("message", message)
                .build();
    }

}