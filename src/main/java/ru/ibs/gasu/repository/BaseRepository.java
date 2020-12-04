package ru.ibs.gasu.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.entity.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by evgeniy on 06.12.18.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    @Query("select e.version from #{#entityName} e where e.id = :id")
    @Transactional(readOnly = true)
    Optional<Long> findLastVersion(@Param("id") Long id);

    @Query("select e from #{#entityName} e where e.obsolete = true")
    @Transactional(readOnly = true)
    List<T> findInactive();

//    @Override
//    @Transactional(readOnly = true)
//    @Query("select e from #{#entityName} e where e.obsolete <> true ")
//    List<T> findAll();

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true")
    List<T> findAll(Sort sort);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id in ?1 and e.obsolete <> true")
    List<T> findAllById(Iterable<Long> iterable);

    @Override
    default void deleteInBatch(Iterable<T> iterable) {
        delete(StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.obsolete=true where e in ?1")
    void delete(List<T> iterable);

    @Override
    @Transactional
    @Modifying
    @Query("update #{#entityName} e set e.obsolete=true")
    void deleteAllInBatch();

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

//    @Override
//    @Transactional(readOnly = true)
//    @Query("select e from #{#entityName} e where e.obsolete <> true ")
//    Page<T> findAll(Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.obsolete <> true")
    T getOne(Long id);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.obsolete <> true")
    Optional<T> findById(Long id);

    @Override
    default boolean existsById(Long id) {
        return findById(id) != null;
    }

    @Override
    @Transactional(readOnly = true)
    @Query("select count(e) from #{#entityName} e where e.obsolete <> true")
    long count();

    @Override
    @Query("update #{#entityName} e set e.obsolete=true where e.id = ?1")
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Override
    @Query("update #{#entityName} e set e.obsolete=true where e = ?1")
    @Transactional
    @Modifying
    void delete(T t);

    @Override
    default void deleteAll(Iterable<? extends T> iterable) {
        delete(StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList()));
    }

//    @Override
//    @Transactional
//    @Modifying
//    @Query("update #{#entityName} e set e.obsolete=true")
//    void deleteAll();

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> Optional<S> findOne(Example<S> example);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> long count(Example<S> example);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.obsolete <> true ")
    <S extends T> boolean exists(Example<S> example);

}
