package ru.ibs.gasu.entity.nplusone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ibs.gasu.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ERodina on 29.10.2020.
 */

@NamedEntityGraphs({
        @NamedEntityGraph(name = "topic.only"),
        @NamedEntityGraph(name = "topic.comments",
                attributeNodes = {@NamedAttributeNode("comments")}),
        @NamedEntityGraph(name = "topic.comments.author",
                attributeNodes = @NamedAttributeNode(value = "comments", subgraph = "subgraph.comments"),
                subgraphs = {
                        @NamedSubgraph(name = "subgraph.comments",
                                attributeNodes = @NamedAttributeNode(value = "author"))})
})
@Entity(name = "NPlusOneTopic")
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
    @JoinColumn(name = "topic_id")
    @OrderBy("text ASC")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    private List<Author> authors = new ArrayList<>();


}