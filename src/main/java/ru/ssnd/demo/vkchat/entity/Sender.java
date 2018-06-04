package ru.ssnd.demo.vkchat.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Created by gzheyts on 5/28/18.
 */
@Data
@Builder
public class Sender {

    private Integer id;

    private String avatarUrl;

    private String name;
}
