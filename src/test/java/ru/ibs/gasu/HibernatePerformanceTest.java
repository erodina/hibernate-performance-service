package ru.ibs.gasu;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.dto.ITopicView;
import ru.ibs.gasu.dto.TopicCustom;
import ru.ibs.gasu.dto.TopicDTO;
import ru.ibs.gasu.entity.nplusone.Topic;
import ru.ibs.gasu.repository.TopicRepository;
import ru.ibs.gasu.service.CriteriaService;
import ru.ibs.gasu.service.HQLService;
import ru.ibs.gasu.service.SpringDataService;
import ru.ibs.gasu.utils.Page;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by erodina on 16.07.20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HibernatePerformanceTest {
    @Autowired
    HQLService hQLService;
    @Autowired
    CriteriaService criteriaService;
    @Autowired
    TopicRepository topicRepo;

    //N+1 problem demo with spring Data
    @Test
    @Transactional(readOnly = true)
    public void getTopicsNPlusOneTest1() {
        Collection<Topic> res = topicRepo.getByTitle_("Статья_1", Topic.class);
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //N+1 problem demo with HQL
    @Test
    @Transactional(readOnly = true)
    public void getTopicsNPlusOneTest2() {
        List<Topic> res = hQLService.getTopicsNPlusOne("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //N+1 problem demo with Criteria
    @Test
    @Transactional(readOnly = true)
    public void getTopicsNPlusOneTest3() {
        List<Topic> res = criteriaService.getTopicsNPlusOne("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }
    //=============================================
    //РЕШЕНИЯ
    //join fetch with hql
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinFetch1() {
        List<Topic> res = hQLService.getTopicsJoinFetch("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }
    //join fetch with criteria
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinFetch2() {
        List<Topic> res = criteriaService.getTopicsJoinFetch("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //join fetch with hql WARN HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinFetch3() {
        Page<Topic> res = hQLService.getTopicsPageJoinFetch("Статья_1", 0, 2);
        res.getContent().stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.getContent().size(), 0);
    }

    //join fetch HibernateException: cannot simultaneously fetch multiple bags (list-set)
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinFetch4() {
        List<ru.ibs.gasu.entity.joinlist.Topic> res = hQLService.getTopicsJoinFetch2ListMultipleBagFetchException("Статья_1"); // demo HibernateException (2 lists)
        //List<Topic> res = hQLService.getTopicsJoinFetchOnlyOneList("Статья_1"); // one List
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //FIX join fetch HibernateException: cannot simultaneously fetch multiple bags (list-set)
    @Test
    @Transactional(readOnly = true)
    public void getTopicsFixMultipleBagFetchException() {
        List<ru.ibs.gasu.entity.joinlist.Topic> res = hQLService.getTopicFixMultipleBagFetchException("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //join dont work (without join fetch) + ignore lazy
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinFetch5() {
        List<Topic> res = hQLService.getTopicsJoinDontWork("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //join one
    @Test
    @Transactional(readOnly = true)
    public void getTopicsJoinOne6() {
        ru.ibs.gasu.entity.join.Topic res = hQLService.getTopicJoinOne(1L); //1sql
        res.getComments().size();
        Assert.assertNotEquals(1, 0);
    }

    //one select
    @Test
    @Transactional(readOnly = true)
    public void getTopicsOneWitoutJoin() {
        //Topic res = topicRepo.getOne(1l); //2 sql
        Topic res = hQLService.getTopicOne(1L);
        res.getComments().size();
        Assert.assertNotEquals(1, 0);
    }

    //select + batch
    @Test
    @Transactional(readOnly = true)
    public void getTopicsBatchSize7() {
        List<ru.ibs.gasu.entity.selectbatch.Topic> res = hQLService.getTopicsBatch("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //select + batch Page
    @Test
    @Transactional(readOnly = true)
    public void getTopicsBarchSizePage8() {
        Page<ru.ibs.gasu.entity.selectbatch.Topic> res = hQLService.getTopicsPageBatch("Статья_1", 0, 2);
        res.getContent().stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.getContent().size(), 0);
    }

    //subselect
    @Test
    @Transactional(readOnly = true)
    public void getTopicsSubselect9() {
        List<ru.ibs.gasu.entity.subselect.Topic> res = hQLService.getTopicsSubselect("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }


    //named entity graph
    @Test
    @Transactional(readOnly = true)
    public void getTopicsNamedEntityGraph10() {
        List<Topic> res = hQLService.getTopicsWithNamedEntityGraph("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //dynamic entity graph
    @Test
    @Transactional(readOnly = true)
    public void getTopicsDynamicEntityGraph11() {
        List<Topic> res = hQLService.getTopicsWithDynamicEntityGraph("Статья_1");
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    // entity graph with spring data
    @Test
    @Transactional(readOnly = true)
    public void getTopicsRepoEntityGraph12() {
        Collection<Topic> res = topicRepo.findByTitle("Статья_1", Topic.class);
        res.stream().forEach(t->t.getComments().size());
        Assert.assertNotEquals(res.size(), 0);
    }

    //PROECTIONS
    @Test
    @Transactional(readOnly = true)
    public void getTopicsNPlusOneMapping() {
        Collection<Topic> res = topicRepo.getByTitle_("Статья_1", Topic.class);
        AtomicInteger ordinal = new AtomicInteger(1);
        res.stream().forEach(t ->{
            log.info("{} {} {} ", ordinal.getAndIncrement() ,t.getTitle(), t.getId());
            t.getAuthors().forEach(c-> log.info("Autor -> {} ", c.getName()));
            t.getComments().forEach(c-> log.info("{} {} {}", c.getText(), c.getTopic().getTitle(), c.getAuthor().getName()));
        });
        Assert.assertNotEquals(res.size(), 0);
    }

    //proections with constructor
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections13() {
        List<TopicDTO> res = hQLService.getTopicsProectionWithConstructor("Статья_1");
        res.stream().forEach(t->log.info("{} {} {}", t.getId(), t.getTitle(), t.getTotal()));
        Assert.assertNotEquals(res.size(), 0);
    }

    //proections with alias
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections14() {
        List<TopicDTO> res = hQLService.getTopicsProectionAlias("Статья_1");
        res.stream().forEach(t->log.info("{} {} {}", t.getId(), t.getTitle(), t.getTotal()));
        Assert.assertNotEquals(res.size(), 0);
    }

    //criteria scalar proections
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections15() {
        List<Object[]> res = criteriaService.getTopicScalarProection("Статья_1");
        res.stream().forEach(o->log.info("{} {}", o[0], o[1]));
        Assert.assertNotEquals(res.size(), 0);
    }
    //criteria dto proections
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections16() {
        List<TopicDTO> res = criteriaService.getTopicDTOProection("Статья_1");
        res.stream().forEach(o->log.info("{} ", o));
        Assert.assertNotEquals(res.size(), 0);
    }

    //spring data proections
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections17() {
        List<ITopicView> res = topicRepo.getByTitle("Статья_1");
        //Collection<ITopicView> res = topicRepo.findByTitle("Статья_1", ITopicView.class);

        res.stream().forEach(t->{
            log.info("{}", t.getFullName());
            t.getComments().stream().forEach(c->log.info("{}", c.getText()));
        });
        Assert.assertNotEquals(1, 0);
    }

    //spring data proections
    @Test
    @Transactional(readOnly = true)
    public void getTopicsProections18() {
        List<TopicCustom> res  = topicRepo.findByTitle2("Статья_1");
        res.stream().forEach(t->log.info("{}", t.getTitle()));
        Assert.assertNotEquals(1, 0);
    }


     private void logResult(List<Topic> res){
         try {
             AtomicInteger ordinal = new AtomicInteger(1);
             res.stream().forEach(t ->{
                 log.info("{} {} {}", ordinal.getAndIncrement() ,t.getTitle(), t.getId());
                 t.getAuthors().forEach(c-> log.info("Autor -> {} ", c.getName()));
                 t.getComments().forEach(c-> log.info("{} {}", c.getText(), t.getId()));
             });
         } catch (Exception e){
             e.printStackTrace();
         }
     }



}
