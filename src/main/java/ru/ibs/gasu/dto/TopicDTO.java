package ru.ibs.gasu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by ERodina on 29.10.2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TopicDTO {
    private Long id;
    private String title;
    private Long total;
}