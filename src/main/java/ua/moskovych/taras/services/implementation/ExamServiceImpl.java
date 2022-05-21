package ua.moskovych.taras.services.implementation;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.moskovych.taras.dao.ClassroomDao;
import ua.moskovych.taras.dao.ExamDao;
import ua.moskovych.taras.dao.GroupDao;
import ua.moskovych.taras.dao.TeacherDao;
import ua.moskovych.taras.entity.*;
import ua.moskovych.taras.entity.enums.ClassroomType;
import ua.moskovych.taras.entity.enums.LessonTime;
import ua.moskovych.taras.services.ExamService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Taras on 14.05.2017.
 */

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamDao examDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private ClassroomDao classroomDao;
    @Autowired
    private ExamDao exa;


    public List<Exam> findAll() {
        return examDao.findAll();
    }

    public void add(Exam exam) {
        examDao.add(exam);
    }

    public List<Exam> findByGroupId(int id) {
        return groupDao.findById(id).getExamList();
    }

    public List<Exam> findByTeacherId(int id) {
        return teacherDao.findById(id).getExamList();
    }

    public List<Object[]> findByT() {
        return examDao.countAnass();
    }


    public void export(HttpServletResponse response,int id) throws IOException {







        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        Image image = Image.getInstance("C:\\Users\\AIT JILAL ANASS\\Desktop\\resu.png");
        image.scaleAbsolute(200, 100);
        image.setTop(233);

        Font font = new Font(Font.HELVETICA, 12, Font.BOLDITALIC);



        List<Exam> users = findByTeacherId(id);
        //
        //
        // adding table rows


        // adding table to document



        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Font fontTitlee = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(14);
        Paragraph paragraph = new Paragraph("Pv d'examen", fontTitle);
        Paragraph paragraphe = new Paragraph("Fiche d'abssence \n Année 2021/2022", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraphe.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);
        List<Exam> examms=findByTeacherId(id);
        for(Exam i:examms) {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            // setting column widths
            table.setWidths(new float[] {6.0f, 6.0f, 6.0f});

            PdfPCell cell = new PdfPCell();

            // table headers
            cell.setPhrase(new Phrase("Num ordre", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase(" Etudiant", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Signature", font));
            table.addCell(cell);

            String salle = i.getClassrooms().toString();
            String sujet = i.getSubjectString().toString();
            String teacherName = i.getTeachersString().toString();
            String students=i.getGroups().getStudents();

            String[] l=students.split("/", -2);
            ArrayList<String> arrOfStr=new ArrayList<>();
            for(String p:l) {
                arrOfStr.add(p);
            }
            int ii=1;
            for (String a : arrOfStr){

                System.out.println(a);
                table.addCell(""+ ii++);
                table.addCell(a);
                table.addCell("");

            }






            Paragraph paragraph22 = new Paragraph("La date:+ " + i.getDate().toString());
            Paragraph paragraph2 = new Paragraph("La salle:+ " + salle);
            Paragraph paragraph3 = new Paragraph("Sujet d'examination:+ " + sujet);
            Paragraph paragraph4 = new Paragraph("Surveillé par + " + teacherName + "   "+"Signature :");
            Paragraph paragraph5 = new Paragraph("\n \n \n \n ");
            Paragraph paragraph6 = new Paragraph("\n \n  ");
            Paragraph paragraph7 = new Paragraph("\n ");
            Paragraph paragraph9 = new Paragraph("Date:Mardi 6 Mai 2022 ");
            paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(image);

            document.add(paragraph);

            document.add(paragraphe);
            document.add(paragraph22);
            document.add(paragraph2);
            document.add(paragraph3);

            document.add(paragraph7);
            document.add(paragraph9);


            document.add(paragraph7);


            document.add(table);
            document.add(paragraph7);
            document.add(paragraph4);

            document.add(paragraph5);
            document.add(paragraph5);
            document.add(paragraph5);
            document.add(paragraph5);
            document.add(paragraph5);







        }


        document.close();
    }


    public void delete(int id) {
        examDao.delete(id);
    }

    public void examGeneration(List<Group> inputGroupList, List<Subject> inputSubjectList, String date1, String date2) {
        Random r = new Random();
        try {
            String a = date1.substring(8, 10);
            String b = date2.substring(8, 10);
            int min = Integer.parseInt(a);
            int max = Integer.parseInt(b);

            int c = 0;
            boolean arr[] = {true, false};
            boolean sw = arr[r.nextInt(arr.length)];

            for (int i = min; i <= max; i++) {


                if (!(inputSubjectList.isEmpty())) {




                    Exam exam = new Exam();

                    if (sw) {
                        if (i % 2 == 0) {
                            addNewExam(exam, date1, i, inputGroupList, inputSubjectList);
                        }
                    }

                    if (!sw) {
                        if (i % 2 != 0) {
                            addNewExam(exam, date1, i, inputGroupList, inputSubjectList);
                        }
                    }

                }
            }
        } catch (Exception e) {
            return;
        }
    }

    private void addNewExam(Exam exam, String date1, int i, List<Group> inputGroupList, List<Subject> inputSubjectList){
        Random r = new Random();

        exam.setDate(date1.substring(0, 8) + i);
        Subject subject = inputSubjectList.get(r.nextInt(inputSubjectList.size()));
        while (!(checkForFreeSubject(subject, date1, getLessonTime(i)))){
            subject = inputSubjectList.get(r.nextInt(inputSubjectList.size()));
        }

        exam.setLessonTime(getLessonTime(r.nextInt(3)));
        exam.setGroups(inputGroupList.get(0));

        Classroom classroom = getClassroom(ClassroomType.LECTURE);
        while (!(checkForClassroom(classroom, getLessonTime(i)))){
            classroom = getClassroom(ClassroomType.LECTURE);
        }
        exam.setClassrooms(classroom);


        exam.setSubjects(subject);
        Teacher teacher = subject.getTeachers().get(0);
        deleteSubjectFromList(inputSubjectList, subject);
        exam.setTeachers(teacher);

        examDao.add(exam);
    }

    private void deleteSubjectFromList(List<Subject> subjects, Subject subject) {
        Iterator<Subject> iterator = subjects.iterator();
        while (iterator.hasNext()) {
            Subject subjectTemp = iterator.next();
            if (subjectTemp == subject) {
                iterator.remove();
            }
        }
    }

    private Classroom getClassroom(ClassroomType classroomType) {
        List<Classroom> classroomList = classroomDao.findByType(classroomType);
        Random r = new Random();
        return classroomList.get(r.nextInt(classroomList.size()));
    }


    private LessonTime getLessonTime(int index) {
        if (index == 0) return LessonTime.FIRST;
        if (index == 1) return LessonTime.SECOND;
        if (index == 2) return LessonTime.THIRD;
        if (index == 3) return LessonTime.FOURTH;
        if (index == 4) return LessonTime.FIFTH;
        else return null;
    }

    private boolean checkForFreeSubject(Subject subject, String date, LessonTime lessonTime){
        boolean b = true;
        List<Group> groupList = groupDao.findAll();
        List<Exam> examList = new ArrayList<Exam>();
            for (Group group : groupList) {
            examList = group.getExamList();
            for (Exam exam : examList) {
                if(exam.getSubjects() == subject && exam.getDate().equals(date) && exam.getLessonTime() == lessonTime) b = false;
            }
        }
        return b;
    }

    private boolean checkForClassroom(Classroom classroom, LessonTime lessonTime){
        boolean b = true;
        List<Group> groupList = groupDao.findAll();
        List<Lesson> lessonList = new ArrayList<Lesson>();
        for (Group group : groupList) {
            lessonList = group.getLessonList();
            for (Lesson lesson : lessonList) {
                if(lesson.getClassrooms() == classroom  && lesson.getLessonTime() == lessonTime) b = false;
            }
        }
        return b;
    }
}
