package ru.ssnd.demo.vkchat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.ssnd.demo.vkchat.entity.Message;

import java.util.List;

/**
 * @author  gzheyts
 */
public interface MessagesRepository extends MongoRepository<Message, String> {

    List<Message> findBySenderIdOrderBySentAtDesc(Integer senderId);

}
