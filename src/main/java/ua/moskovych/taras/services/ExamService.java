package ua.moskovych.taras.services;

import ua.moskovych.taras.entity.Exam;
import ua.moskovych.taras.entity.Group;
import ua.moskovych.taras.entity.Subject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Taras on 14.05.2017.
 */
public interface ExamService {

    List<Exam> findAll();
    void add(Exam exam);
    List<Exam> findByGroupId(int id);
    List<Exam> findByTeacherId(int id);
    List<Object[]> findByT();
    void export(HttpServletResponse response,int id) throws IOException;

    void delete(int id);
    void examGeneration(List<Group> inputGroupList, List<Subject> inputSubjectList, String date1, String date2);
}
