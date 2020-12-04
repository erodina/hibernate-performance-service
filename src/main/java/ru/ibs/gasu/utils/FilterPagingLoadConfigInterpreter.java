package ru.ibs.gasu.utils;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class FilterPagingLoadConfigInterpreter<T> {

    public Specification<T> toSpecification(FilterPagingLoadConfig fpd) {
        return toSpecification(null, fpd.getFilters(), fpd.isDisjunction());
    }

    private Specification<T> toSpecification(Specification<T> s, List<FilterConfig> fs, boolean isDisjunction) {
        if (s == null) s = (root, cq, cb) -> isDisjunction ? cb.disjunction() : cb.conjunction();
        if (fs == null || fs.size() == 0) return s;

        for (FilterConfig f : fs) {
            if (f.getValue() != null && XMLGregorianCalendarImpl.class.isAssignableFrom(f.getValue().getClass())) {
                f.setValue(((XMLGregorianCalendarImpl) f.getValue()).toGregorianCalendar().getTime());
            }

            if (!isEmpty(f.getValues())) {
                f.setValue(f.getValues());
            }

            Specification<T> ts = (root, cq, cb) -> f.getComparison().getPredicate(cb, this.getExpression(root, f.getField()), f.getValue());

            if (f.getFilters() != null && f.getFilters().size() > 0)
                ts = toSpecification(ts, f.getFilters(), f.isDisjunction());

            s = isDisjunction ? s.or(ts) : s.and(ts);
        }
        return s;
    }

    public PageRequest toPageRequest(FilterPagingLoadConfig fpd) {
        return PageRequest.of(fpd.getOffset() / fpd.getLimit(), fpd.getLimit(), this.toSort(fpd.getSortInfo()));
    }

    private Sort toSort(List<SortInfo> si) {
        Sort s = Sort.unsorted();
        if (si == null || si.size() == 0) return s;
        for (SortInfo e : si) {
            s = s.and(Sort.by(e.getDirection().equals(SortInfo.Direction.DESC) ?
                    Sort.Direction.DESC : Sort.Direction.ASC, e.getField()));
        }
        return s;
    }

    private Expression<?> getExpression(Root<T> root, String s) {
        Path<Object> path = null;
        String[] split = s.split("\\.");
        for (int i = 0; i < split.length; i++) {
            if (i == 0) path = root.get(split[i]);
            else path = path.get(split[i]);
        }
        return path;
    }

}
