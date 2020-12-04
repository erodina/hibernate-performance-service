package ru.ibs.gasu.entity.nplusone;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ibs.gasu.entity.BaseEntity;
import ru.ibs.gasu.entity.nplusone.Topic;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by ERodina on 29.10.2020.
 */
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Author extends BaseEntity {

    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    public Author(String name){
        this.name = name;
    }

}