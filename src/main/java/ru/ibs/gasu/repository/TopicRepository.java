package ru.ibs.gasu.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.dto.ITopicView;
import ru.ibs.gasu.dto.TopicCustom;
import ru.ibs.gasu.dto.TopicDTO;
import ru.ibs.gasu.entity.nplusone.Topic;
import ru.ibs.gasu.utils.Page;

import java.util.Collection;
import java.util.List;


@Transactional(value = "transactionManager")
public interface TopicRepository extends BaseRepository<Topic> {
    //@EntityGraph(attributePaths = {"comments"})
    //@EntityGraph(value = "topic.comments")
    @EntityGraph(value = "topic.comments.author")
    //@EntityGraph(value = "topic.only")
    <T> Collection<T> findByTitle(String title, Class<T> type);

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.title = ?1")
    List<ITopicView> getByTitle(String title);

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.title = ?1")
    <T> Collection<T> getByTitle_(String title, Class<T> type);

    @Query("select new ru.ibs.gasu.dto.TopicCustom(topic.id,topic.title) from NPlusOneTopic topic where topic.title = ?1")
    List<TopicCustom> findByTitle2(String title);

    //    @Query("SELECT b.id, b.title FROM Topic b")
    //    List<Object[]> getIdAndTitle123();
    //    @Query("select a from Topic a left join fetch a.comments")
    //    List<Topic> findByTitleEntity(String title);

    }
