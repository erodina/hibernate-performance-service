package ru.ibs.gasu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.entity.nplusone.Author;
import ru.ibs.gasu.entity.nplusone.Comment;


import ru.ibs.gasu.entity.nplusone.Topic;
import ru.ibs.gasu.repository.TopicRepository;
import ru.ibs.gasu.utils.FilterPagingLoadConfig;
import ru.ibs.gasu.utils.FilterPagingLoadConfigInterpreter;
import ru.ibs.gasu.utils.Page;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by erodina on 28.05.19.
 */
@Service
@Transactional(value = "transactionManager")
@Slf4j
public class SpringDataService {
    @Autowired
    TopicRepository topicRepo;


    public List<Topic> getTopics(){
        List<Topic> res = topicRepo.findAll();
        logResult(res);
        return res;
    }

    private void logResult(List<Topic> res){
        try {
            AtomicInteger ordinal = new AtomicInteger(1);
            res.stream().forEach(t ->{
                log.info("{} Topic-> {} ", ordinal.getAndIncrement() ,t.getTitle());
                t.getAuthors().forEach(c-> log.info("Autor -> {} ", c.getName()));
                t.getComments().forEach(c-> log.info("Comment -> {}", c.getText()));
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Page<Topic> getTopicsPage(FilterPagingLoadConfig iFilterPagingLoadConfig) {

        FilterPagingLoadConfigInterpreter<Topic> fplc = new FilterPagingLoadConfigInterpreter<>();
        Specification<Topic> specification = fplc.toSpecification(iFilterPagingLoadConfig);
        PageRequest pageRequest = fplc.toPageRequest(iFilterPagingLoadConfig);
        Page<Topic> page = new Page<>(topicRepo.findAll(specification, pageRequest));
        page.setPage(page.getPage());
        page.setTotal(page.getTotal());
        page.setContent(page.getContent());
        return page;
    }

    public Topic saveTopic(Topic topic){
        return topicRepo.saveAndFlush(topic);
    }

    public void deleteAllTopics(){
        topicRepo.deleteAll();
    }

    public void createNewTopics(){
        deleteAllTopics();
        for (int i = 1; i <= 3 ; i++){
            Topic t = new Topic("Статья_" + i);
            for (int j = 1; j <= 3; j++){
                Comment comment = new Comment("Комментарий_" + j  + " к статье '" + t.getTitle() + "'");
                comment.setAuthor(new Author("Автор комментария_" + j));
                t.getComments().add(comment);
            }

            for (int j = 1; j <= 2; j++){
                Author author = new Author("Автор_" + j  + " статьи '" + t.getTitle() + "'");
                t.getAuthors().add(author);
            }
            saveTopic(t);

        }

        Topic t = new Topic("Статья_" + 1);
        for (int j = 1; j <= 2; j++){
            Comment comment = new Comment("Комментарий!!!_" + j  + " к статье '" + t.getTitle() + "'");
            comment.setAuthor(new Author("Автор комментария!!!_" + j));
            t.getComments().add(comment);
        }

        for (int j = 1; j <= 1; j++){
            Author author = new Author("Автор!!!_" + j  + " статьи '" + t.getTitle() + "'");
            t.getAuthors().add(author);
        }
        saveTopic(t);

        t = new Topic("Статья_" + 1);
        for (int j = 1; j <= 3; j++){
            Comment comment = new Comment("Комментарий_" + j  + " к статье '" + t.getTitle() + "'");
            comment.setAuthor(new Author("Автор комментария_" + j));
            t.getComments().add(comment);
        }

        for (int j = 1; j <= 2; j++){
            Author author = new Author("Автор_" + j  + " статьи '" + t.getTitle() + "'");
            t.getAuthors().add(author);
        }

        saveTopic(t);
    }



}
