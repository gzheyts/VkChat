package ru.ssnd.demo.vkchat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.ssnd.demo.vkchat.entity.Message;

public interface MessagesRepository extends MongoRepository<Message, String> {

}
