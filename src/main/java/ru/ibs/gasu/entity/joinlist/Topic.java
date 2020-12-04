package ru.ibs.gasu.entity.joinlist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ibs.gasu.entity.BaseEntity;
import ru.ibs.gasu.entity.nplusone.Author;
import ru.ibs.gasu.entity.nplusone.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERodina on 29.10.2020.
 */
@Getter
@Setter
@Entity(name = "JoinListTopic")
@Table(name = "Topic")
@NoArgsConstructor
public class Topic extends BaseEntity {

    private String title;
    public Topic(String title){
        this.title = title;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    @OrderBy("text ASC")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    private List<Author> authors = new ArrayList<>();


}