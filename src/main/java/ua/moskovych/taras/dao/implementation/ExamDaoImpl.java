package ua.moskovych.taras.dao.implementation;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.moskovych.taras.dao.ExamDao;
import ua.moskovych.taras.entity.Exam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * Created by Taras on 14.05.2017.
 */

@Repository
public class ExamDaoImpl implements ExamDao {

    @PersistenceContext(unitName = "Main")
    private EntityManager entityManager;

    @Transactional
    public List<Object[]> countAnass() {
        return entityManager.createQuery("SELECT count(m),m.teachers.name from Exam m GROUP BY m.teachers.id").getResultList();
    }

    @Transactional
    public List<Exam> findAll() {
        return entityManager.createQuery("SELECT e FROM Exam e ORDER BY e.subjects.id").getResultList();
    }

    @Transactional
    public void add(Exam exam) {
        entityManager.persist(exam);
    }

    @Transactional
    public void delete(int id) {
        entityManager.remove(entityManager.find(Exam.class, id));
    }
}
