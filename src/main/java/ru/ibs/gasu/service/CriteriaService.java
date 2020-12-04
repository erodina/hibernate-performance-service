package ru.ibs.gasu.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.dto.TopicDTO;
import ru.ibs.gasu.entity.nplusone.Comment;
import ru.ibs.gasu.entity.nplusone.Topic;
import ru.ibs.gasu.utils.Page;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;


/**
 * Created by erodina on 28.05.19.
 */
@Service
@Transactional(value = "transactionManager")
@Slf4j
public class CriteriaService {
    @PersistenceContext (type = PersistenceContextType.EXTENDED) //расширенная сессия
    private EntityManager em;

    public List<Topic> getTopicsNPlusOne(String title){
        Session session = em.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Topic> query = cb.createQuery(Topic.class);
        Root<Topic> root = query.from(Topic.class);
        query.select(root).where(cb.and(cb.equal(root.get("title"), title)));
        List<Topic> list  = session.createQuery(query).list();
        return list;
    }

    //декартово произведение (без distinct)
    public List<Topic> getTopicsJoinFetch(String title){
        Session session = em.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Topic> query = cb.createQuery(Topic.class);
        Root<Topic> root = query.from(Topic.class);
        root.fetch("comments", JoinType.INNER);
        query.select(root).where(cb.and(cb.equal(root.get("title"), title)));//.distinct(true);

        //List<Topic> list  = session.createQuery(query).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        List<Topic> list  = session.createQuery(query).list();
        return list;
    }

    public List<Topic> getTopicsWithEntityGraph(){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Topic> query = builder.createQuery(Topic.class);
        Root<Topic> root = query.from(Topic.class);
        CriteriaQuery<Topic> criteriaQuery =
                query.select(root);//.where(builder.and(builder.equal(root.get("id"), 1L)));
        List<Topic> list = em.createQuery(criteriaQuery)
                .setHint("javax.persistence.fetchgraph",
                        em.getEntityGraph("topic.comments")).getResultList();
        return list;
    }

    public Page<Topic> getTopicsPage(int offset, int limit){
        Session session = em.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Topic> query = cb.createQuery(Topic.class);
        Root<Topic> root = query.from(Topic.class);
        PageRequest pr = PageRequest.of(offset / limit, limit, Sort.unsorted());
        root.fetch("comments", JoinType.INNER);
        //root.fetch("authors", JoinType.INNER);

        query.select(root).distinct(true);

        Query q = session.createQuery(query);


        q.setFirstResult(pr.getPageNumber() * pr.getPageSize());
        q.setMaxResults(pr.getPageSize());

        List<Topic> content = q.getResultList();

        Page<Topic> page = new Page<>();
        page.setContent(content);
        page.setPage(pr.getPageNumber());

        long rowCount = getTotalRowCount(session, cb, Topic.class, null);
        page.setTotal(rowCount);


        return page;
    }

    private long getTotalRowCount(Session s, CriteriaBuilder cb, Class Class, Predicate p) {
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        ccq.select(cb.count(ccq.from(Class)));
        if (p != null)
            ccq.where(p);

        return s.createQuery(ccq)
                .list()
                .stream()
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
    }

    public List<Object[]> getTopicScalarProection(String title){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Topic> root = query.from(Topic.class);
        query.multiselect(root.get("id"), root.get("title")).where(cb.and(cb.equal(root.get("title"), title)));;
        List<Object[]> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    public List<TopicDTO> getTopicDTOProection(String title){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TopicDTO> query = cb.createQuery(TopicDTO.class);
        Root<Topic> root = query.from(Topic.class);
        query.multiselect(root.get("id"), root.get("title"), root.get("id")).where(cb.and(cb.equal(root.get("title"), title)));;
        TypedQuery<TopicDTO> q = em.createQuery(query);
        //q.setParameter(paramTitle, "%Hibernate Tips%");
        List<TopicDTO> res = q.getResultList();
        return res;
    }


}
