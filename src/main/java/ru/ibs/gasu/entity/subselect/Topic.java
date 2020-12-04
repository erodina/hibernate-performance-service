package ru.ibs.gasu.entity.subselect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.ibs.gasu.entity.nplusone.Author;
import ru.ibs.gasu.entity.BaseEntity;
import ru.ibs.gasu.entity.nplusone.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ERodina on 29.10.2020.
 */
@Entity(name = "SubselectTopic")
@Table(name = "Topic")
@NoArgsConstructor
@Getter
@Setter
public class Topic extends  BaseEntity{

    private String title;
    public Topic(String title){
        this.title = title;
    }

    @OneToMany//(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    @OrderBy("text ASC")
    @Fetch(FetchMode.SUBSELECT)
    private Set<Comment> comments = new HashSet<>();


    @OneToMany//(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Author> authors = new ArrayList<>();


}