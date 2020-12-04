package ru.ibs.gasu.entity.nplusone;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ibs.gasu.entity.BaseEntity;
import ru.ibs.gasu.entity.nplusone.Author;
import ru.ibs.gasu.entity.nplusone.Topic;

import javax.persistence.*;

/**
 * Created by ERodina on 29.10.2020.
 */
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Comment extends BaseEntity {

    private String text;
    @OneToOne(fetch = FetchType.LAZY)
    private Topic topic;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Author author;

    public Comment(String text){
        this.text=text;
    }

}