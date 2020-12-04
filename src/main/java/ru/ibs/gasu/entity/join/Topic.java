package ru.ibs.gasu.entity.join;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.ibs.gasu.entity.BaseEntity;
import ru.ibs.gasu.entity.nplusone.Author;
import ru.ibs.gasu.entity.nplusone.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ERodina on 29.10.2020.
 */
@Entity(name = "JoinTopic")
@Table(name = "Topic")
@NoArgsConstructor
@Getter
@Setter
public class Topic extends BaseEntity {
    private String title;
    public Topic(String title){
        this.title = title;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "topic_id")
    @OrderBy("text ASC")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "topic_id")
    private Set<Author> authors = new HashSet<>();


}