package ua.moskovych.taras.dao.implementation;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.moskovych.taras.dao.ProgramDao;
import ua.moskovych.taras.entity.Group;
import ua.moskovych.taras.entity.Program;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taras on 13.05.2017.
 */
@Repository
public class ProgramDaoImpl implements ProgramDao {

    @Override
    public void saveAll(List<Program> tutorials) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TDEMSPU");
        EntityManager em = emf.createEntityManager();


        em.getTransaction().begin();

        List<Program> tempEnqList =tutorials ;
        for (Iterator<Program> it = tempEnqList.iterator(); it.hasNext();) {
            Program enquiry = it.next();

            em.persist(enquiry);
            em.flush();
            em.clear();
        }

        em.getTransaction().commit();

    }


    @PersistenceContext(unitName = "Main")
    private EntityManager entityManager;

    @Transactional
    public void add(Program program) {
        entityManager.persist(program);
    }

    @Transactional
    public void edit(Program program) {
        entityManager.merge(program);
    }

    @Transactional
    public void delete(int id) {
        entityManager.remove(entityManager.find(Program.class, id));
    }

    @Transactional
    public List<Program> findAll() {
        return entityManager.createQuery("select P from Program P").getResultList();
    }

    @Transactional
    public Program find(int id) {
        return entityManager.find(Program.class, id);
    }
}
