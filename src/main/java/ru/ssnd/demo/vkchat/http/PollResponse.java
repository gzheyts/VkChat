package ru.ssnd.demo.vkchat.http;

import lombok.Builder;
import ru.ssnd.demo.vkchat.entity.Message;

import java.util.List;

/**
 * Created by gzheyts on 5/28/18.
 */
@Builder
public class PollResponse {
    private List<Message> messages;
}
