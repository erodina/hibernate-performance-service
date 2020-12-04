package ru.ibs.gasu.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FilterConfig implements Serializable {

    private String field;
    private Comparison comparison;
    private Object value;
    private List<Object> values;
    private boolean disjunction;
    private List<FilterConfig> filters = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public enum Comparison {
        EQUAL {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.equal(e, o);
            }
        },
        NOT_EQUAL {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.notEqual(e, o);
            }
        },
        LIKE {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.like((Expression<String>) e, ((String) o));
            }
        },
        GREATER_THAN {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.greaterThan((Expression<Comparable>) e, (Comparable) o);
            }
        },
        LESS_THAN {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.lessThan((Expression<Comparable>) e, (Comparable) o);
            }
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.greaterThanOrEqualTo((Expression<Comparable>) e, (Comparable) o);
            }
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.lessThanOrEqualTo((Expression<Comparable>) e, (Comparable) o);
            }
        },
        IN {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                CriteriaBuilder.In<Object> in = cb.in(e);
                ((Collection<?>) o).forEach(in::value);
                return in;
            }
        },
        BETWEEN {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                Iterator<?> i = ((Collection<?>) o).iterator();
                Object o1 = i.next();
                Object o2 = i.next();
                return cb.between((Expression<Comparable>) e, (Comparable) o1, (Comparable) o2);
            }
        },
        IS_NULL {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.isNull(e);
            }
        },
        IS_NOT_NULL {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.isNotNull(e);
            }
        },
        CONTAINS {
            @Override
            public Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o) {
                return cb.like((Expression<String>) e, "%" + o + "%");
            }
        };
        public abstract Predicate getPredicate(CriteriaBuilder cb, Expression<?> e, Object o);

    }

    public FilterConfig() {
    }

    public FilterConfig(String field, String value) {
        this.field = field;
        this.comparison = Comparison.EQUAL;
        this.value = value;
    }

    public FilterConfig(String field, Comparison comparison, String value) {
        this.field = field;
        this.comparison = comparison;
        this.value = value;
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<FilterConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterConfig> includedFilters) {
        this.filters = includedFilters;
    }

    public boolean isDisjunction() {
        return disjunction;
    }

    public FilterConfig setDisjunction(boolean disjunction) {
        this.disjunction = disjunction;
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
