package ru.ibs.gasu.dto;



import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

/**
 * Created by ERodina on 30.11.2020.
 */
public interface ITopicView {
    Long getId();
    String getTitle();
    @Value("#{target.id + ' ' + target.title}")
    String getFullName();

    Set<ICommentView> getComments();
}
