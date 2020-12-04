package ru.ibs.gasu.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.ibs.gasu.entity.BaseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by evgeniy on 10.12.18.
 */
public class BaseSpecification<T extends BaseEntity> implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteria, CriteriaBuilder criteriaBuilder) {
        return null;
    }

}
