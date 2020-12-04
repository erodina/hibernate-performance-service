package ru.ibs.gasu.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.dto.TopicDTO;
import ru.ibs.gasu.entity.nplusone.Topic;
import ru.ibs.gasu.utils.Page;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * Created by erodina on 28.05.19.
 */
@Service
@Transactional(value = "transactionManager")
@Slf4j
public class HQLService {
    @PersistenceContext //(type = PersistenceContextType.EXTENDED) //расширенная сессия
    private EntityManager em;

    //n+1 problem
    public List<Topic> getTopicsNPlusOne(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                "where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }

    //join fetch декартово произведение
    public List<Topic> getTopicsJoinFetch(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                "JOIN fetch topic.comments where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }

    //join fetch WARN HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
    public Page<Topic> getTopicsPageJoinFetch(String title, int offset, int limit){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                "JOIN fetch topic.comments where topic.title = :title")
                .setParameter("title", title);

        PageRequest pr = PageRequest.of(offset / limit, limit, Sort.unsorted());
        q.setFirstResult(pr.getPageNumber() * pr.getPageSize());
        q.setMaxResults(pr.getPageSize());
        List<Topic> content = q.getResultList();
        Page<Topic> page = new Page<>();
        page.setContent(content);
        page.setPage(pr.getPageNumber());
        String countQ = "Select count (topic.id) from ru.ibs.gasu.entity.nplusone.Topic topic";
        Query countQuery = em.createQuery(countQ);
        Long count = (Long) countQuery.getSingleResult();
        page.setTotal(count);
        return page;

    }


    //join fetch HibernateException: cannot simultaneously fetch multiple bags (list-set)
    public List<ru.ibs.gasu.entity.joinlist.Topic> getTopicsJoinFetch2ListMultipleBagFetchException(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.joinlist.Topic topic " +
                "JOIN fetch topic.comments  JOIN fetch topic.authors where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }

    //join fetch only one list
    public List<Topic> getTopicsJoinFetchOnlyOneList(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                "JOIN fetch topic.comments  JOIN fetch topic.authors where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }

    public List<ru.ibs.gasu.entity.joinlist.Topic> getTopicFixMultipleBagFetchException(String title){
        List<ru.ibs.gasu.entity.joinlist.Topic> res = em.createQuery("select distinct topic from ru.ibs.gasu.entity.joinlist.Topic topic " +
                "left JOIN fetch topic.comments where topic.title = :title")
                .setParameter("title", title)
                .getResultList();

        res = em.createQuery("select distinct topic from ru.ibs.gasu.entity.joinlist.Topic topic " +
                "left JOIN fetch topic.authors where topic in :topics")
                .setParameter("topics", res)

                .getResultList();
        return res;
    }


    //join dont work
    public List<Topic> getTopicsJoinDontWork(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.join.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }

    //join one
    public ru.ibs.gasu.entity.join.Topic getTopicJoinOne(Long id){
        return em.find(ru.ibs.gasu.entity.join.Topic.class,id);

    }
    //one select
    public Topic getTopicOne(Long id){
        return em.find(Topic.class,id);

    }

    //one subselect
    public ru.ibs.gasu.entity.subselect.Topic getTopicOneSubselect(Long id){
        return em.find(ru.ibs.gasu.entity.subselect.Topic.class,id);

    }

    //select + batch
    public List<ru.ibs.gasu.entity.selectbatch.Topic> getTopicsBatch(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.selectbatch.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();

    }
    //select + batch page
    public Page<ru.ibs.gasu.entity.selectbatch.Topic> getTopicsPageBatch(String title, int offset, int limit){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.selectbatch.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);

        PageRequest pr = PageRequest.of(offset / limit, limit, Sort.unsorted());
        q.setFirstResult(pr.getPageNumber() * pr.getPageSize());
        q.setMaxResults(pr.getPageSize());
        List<ru.ibs.gasu.entity.selectbatch.Topic> content = q.getResultList();
        Page<ru.ibs.gasu.entity.selectbatch.Topic> page = new Page<>();
        page.setContent(content);
        page.setPage(pr.getPageNumber());
        String countQ = "Select count (topic.id) from ru.ibs.gasu.entity.selectbatch.Topic topic";
        Query countQuery = em.createQuery(countQ);
        Long count = (Long) countQuery.getSingleResult();
        page.setTotal(count);
        return page;

    }

    //subselect
    public List<ru.ibs.gasu.entity.subselect.Topic> getTopicsSubselect(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.subselect.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);
        return q.getResultList();
    }

    //named entity graph (lazy)
    public List<Topic> getTopicsWithNamedEntityGraph(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);
        q.setHint("javax.persistence.fetchgraph",em.getEntityGraph("topic.comments"));
        return q.getResultList();

    }

//    public Topic getTopic(Long id){
//        Topic topic =  em.find(Topic.class,id);
//        Collections.singletonMap("javax.persistence.fetchgraph",
//                em.getEntityGraph("topic.comments"));
//        return  topic;
//    }


    //dynamic entity graph (lazy)
    public List<Topic> getTopicsWithDynamicEntityGraph(String title){
        Query q = em.createQuery("select topic from ru.ibs.gasu.entity.nplusone.Topic topic " +
                " where topic.title = :title")
                .setParameter("title", title);
        EntityGraph<Topic> graph = em.createEntityGraph(Topic.class);
        graph.addSubgraph("comments").addAttributeNodes("author");
        q.setHint("javax.persistence.fetchgraph",graph);
        return q.getResultList();

    }

    //proections with constructor
    public List<TopicDTO> getTopicsProectionWithConstructor(String title) {
        Session session = em.unwrap(Session.class);
        Query q = session.createQuery("select new ru.ibs.gasu.dto.TopicDTO(topic.id,topic.title,sum(comments.id)) " +
                "from NPlusOneTopic topic " +
                "left join topic.comments comments " +
                "where topic.title=:title group by topic.id")
                .setParameter("title", title);

        return q.getResultList();
    }

    //proections with alias
    public List<TopicDTO> getTopicsProectionAlias(String title) {
        Session session = em.unwrap(Session.class);
        Query q = session.createQuery("select topic.id as id ,topic.title as title ,sum(comments.id) as total" +
                " from NPlusOneTopic topic " +
                "left join topic.comments comments " +
                "where topic.title=:title group by topic.id")
                .setParameter("title", title)
                .setResultTransformer(Transformers.aliasToBean(TopicDTO.class));
        return q.getResultList();
    }

}
