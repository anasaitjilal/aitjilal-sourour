package ua.moskovych.taras.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.moskovych.taras.Formatters.GroupSearchForm;
import ua.moskovych.taras.Formatters.GroupSearchFormList;
import ua.moskovych.taras.Formatters.GroupSubjectSearchFormSet;
import ua.moskovych.taras.Formatters.TeacherSearchFormSet;
import ua.moskovych.taras.dao.GroupDao;
import ua.moskovych.taras.entity.enums.Day;
import ua.moskovych.taras.entity.enums.LessonMode;
import ua.moskovych.taras.entity.enums.LessonTime;
import ua.moskovych.taras.entity.*;
import ua.moskovych.taras.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TimetableController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ExamService examService;
    @Autowired
    private ProgramService programService;

    private List<Lesson> lessonList = new ArrayList<Lesson>();
    private List<Exam> examList = new ArrayList<Exam>();

    @RequestMapping(value = "/timetable", method = RequestMethod.GET)
    public String timetable(Model model) {
        sendNew(model);
        return "timetable";
    }

    @RequestMapping(value = "/timetable/exam", method = RequestMethod.GET)
    public String timetableExam(Model model) {
        model.addAttribute("date", new Date());
        sendNew(model);
        return "exam";
    }

    @RequestMapping(value = "/timetable/group/{groupId}", method = RequestMethod.GET)
    public String forGroup(@PathVariable("groupId") Integer groupId, Model model) {
        model.addAttribute("daysOfWeek", Day.values());
        model.addAttribute("lessonTime", LessonTime.values());
        model.addAttribute("lessonMode", LessonMode.values());
        sendNew(model);
        lessonList = lessonService.findByGroupId(groupId);
        Collections.sort(lessonList, lessonComparator);
        model.addAttribute("lessons", lessonList);
        model.addAttribute("mode", "group");
        model.addAttribute("name", groupService.getName(groupId));
        return "timetable";
    }

    @RequestMapping(value = "/timetable/teacher/{teacherId}", method = RequestMethod.GET)
    public String forTeacher(@PathVariable("teacherId") Integer teacherId, Model model) {
        model.addAttribute("daysOfWeek", Day.values());
        model.addAttribute("lessonTime", LessonTime.values());
        model.addAttribute("lessonMode", LessonMode.values());
        sendNew(model);
        lessonList = lessonService.findByTeacherId(teacherId);
        Collections.sort(lessonList, lessonComparator);
        model.addAttribute("lessons", lessonList);
        model.addAttribute("mode", "teacher");
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacherName", teacher.getName());
        model.addAttribute("teacherSecondname", teacher.getSecondName());
        model.addAttribute("name", teacherService.getName(teacherId));
        return "timetable";
    }

    @RequestMapping(value = "/timetable/exam/group/{groupId}", method = RequestMethod.GET)
    public String examForGroup(@PathVariable("groupId") Integer groupId, Model model) {
        model.addAttribute("daysOfWeek", Day.values());
        model.addAttribute("lessonTime", LessonTime.values());
        sendNew(model);
        examList = examService.findByGroupId(groupId);
        model.addAttribute("exams", examList);
        model.addAttribute("mode", "group");
        model.addAttribute("name", groupService.getName(groupId));
        return "exam";
    }

    @RequestMapping(value = "/timetable/exam/teacher/{teacherId}", method = RequestMethod.GET)
    public String examForTeacher(@PathVariable("teacherId") Integer teacherId, Model model) {
        model.addAttribute("daysOfWeek", Day.values());
        model.addAttribute("lessonTime", LessonTime.values());
        sendNew(model);
        examList = examService.findByTeacherId(teacherId);
        model.addAttribute("exams", examList);
        model.addAttribute("mode", "teacher");
        Teacher teacher = teacherService.findById(teacherId);
        model.addAttribute("teacherName", teacher.getName());
        model.addAttribute("teacherSecondname", teacher.getSecondName());
        model.addAttribute("name", teacherService.getName(teacherId));
        return "exam";
    }

    @RequestMapping(value = "**/generate", method = RequestMethod.POST)
    public String timetableGeneration(@ModelAttribute("groupSubjectSearchFormSet") GroupSubjectSearchFormSet groupSubjectSearchFormSet, HttpServletRequest request, BindingResult result){
        double startTime = System.currentTimeMillis();

        List<Group> groups = groupSubjectSearchFormSet.getGroups();
        for (Group group : groups) {
            List<Lesson> lessonList = group.getLessonList();
            for (Lesson lesson : lessonList) {
                lessonService.delete(lesson.getId());
            }
        }

        lessonService.generate(groupSubjectSearchFormSet.getGroups(), groupSubjectSearchFormSet.getSubjects());
        /*
        List<Group> groupList = groupSubjectSearchFormSet.getGroups();
        while (i < groupList.size()) {
            lessonService.generation(groupSubjectSearchFormSet.getSubjects(), groupList.get(i));
            i++;
        }
        */

        double timeSpent = System.currentTimeMillis() - startTime;
        System.out.println("Time generation: " + timeSpent/1000 + " sec.");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/examGeneration", method = RequestMethod.POST)
        public String examGeneration(@ModelAttribute("groupSubjectSearchFormSet") GroupSubjectSearchFormSet groupSubjectSearchFormSet, @RequestParam("date3") String date1, @RequestParam("date2") String date2, HttpServletRequest request, BindingResult result){//        List<Group> groups = groupSubjectSearchFormSet.getGroups();
        for (Group group : groupSubjectSearchFormSet.getGroups()) {
            List<Exam> examList = group.getExamList();
            for (Exam exam : examList) {
                examService.delete(exam.getId());
            }
        }
        examService.examGeneration(groupSubjectSearchFormSet.getGroups(), groupSubjectSearchFormSet.getSubjects(), date1, date2);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/generate/clean", method = RequestMethod.POST)
    public String cleanTimetable(@ModelAttribute("groupSearchForm") GroupSearchForm groupSearchForm, HttpServletRequest request, BindingResult result){
        List<Lesson> lessonList = lessonService.findByGroupId(groupSearchForm.getGroup().getId());
        for (Lesson lesson : lessonList) {
            lessonService.delete(lesson.getId());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/generate/cleanExam", method = RequestMethod.POST)
    public String cleanExam(@ModelAttribute("groupSearchForm") GroupSearchForm groupSearchForm, HttpServletRequest request, BindingResult result){
        List<Exam> examList = examService.findByGroupId(groupSearchForm.getGroup().getId());
        for (Exam exam : examList) {
            examService.delete(exam.getId());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/add/lesson", method = RequestMethod.POST)
    public String addLesson(@ModelAttribute("lesson") Lesson lesson, HttpServletRequest request, BindingResult result) {
       try {
           if (lessonService.checkForFreeLesson(lesson)) lessonService.add(lesson);
       }
       catch (Exception e){}
       String referer = request.getHeader("Referer");
       return "redirect:" + referer;
    }

    @RequestMapping(value = "**/delete/lesson/{lessonId}/")
    public String deleteLesson(@PathVariable("lessonId") Integer id, HttpServletRequest request) {
        lessonService.delete(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/add/exam", method = RequestMethod.POST)
    public String addExam(@ModelAttribute("exam") Exam exam, HttpServletRequest request, BindingResult result) {
        for(int i=0;i<subjectService.findAll().size()-1;i++) {
            Integer ade = new Random().nextInt(teacherService.findAll().size() - 1);
             Integer adee = new Random().nextInt(teacherService.findAll().size() - 1);
            Integer adeee = new Random().nextInt(teacherService.findAll().size() - 1);
            Integer ada = new Random().nextInt(subjectService.findAll().size() - 1);
            Integer adaa = new Random().nextInt(subjectService.findAll().size() - 1);
            Integer adaaa = new Random().nextInt(subjectService.findAll().size() - 1);
            Integer adea = new Random().nextInt(classroomService.findAll().size() - 1);
            Integer adeea = new Random().nextInt(classroomService.findAll().size() - 1);
            Integer adeeea = new Random().nextInt(classroomService.findAll().size() - 1);

            Integer adeao = new Random().nextInt(lessonService.findAll().size() - 1);
            Integer adeeao = new Random().nextInt(lessonService.findAll().size() - 1);
            Integer adeeeao = new Random().nextInt(lessonService.findAll().size() - 1);
            Integer ad = new Random().nextInt(6 - 1 + 1) + 1;
            Exam ans = new Exam(exam.getGroups(), teacherService.findAll().get(ade), subjectService.findAll().get(i), classroomService.findAll().get(adea), lessonService.findAll().get(adeao).getLessonTime(), exam.getDate());

            examService.add(ans);

        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @RequestMapping(value = "**/addd/examm", method = RequestMethod.POST)
    public String addExamm(@ModelAttribute("exam") Exam exam, HttpServletRequest request, BindingResult result) {

        examService.add(exam);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/delete/exam/{examId}/")
    public String deleteExam(@PathVariable("examId") Integer id, HttpServletRequest request) {
        examService.delete(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/add/teacher-subject", method = RequestMethod.POST)
    public String addTeacherToSubject(@RequestParam("id") Integer id, @ModelAttribute("teacherSearchFormList") TeacherSearchFormSet teacherSearchFormList, HttpServletRequest request, BindingResult result) {
        try {
            List<Teacher> teacherList = teacherSearchFormList.getTeachers();
            for (Teacher teacher : teacherList) {
               subjectService.addToSubjectNewTeacher(id, teacher);
            }
        }
        catch (Exception e){
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/add/curriculum-group", method = RequestMethod.POST)
    public String addGroupToCurriculum(@RequestParam("id") Integer id, @ModelAttribute("groupSearchFormList") GroupSearchFormList groupSearchFormList, HttpServletRequest request, BindingResult result) {
        for (Group group : groupSearchFormList.getGroupList()) {
            programService.addGroups(id, group);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/removeAllTeachers", method = RequestMethod.POST)
    public String removeAllTeachers(@RequestParam("id") Integer id, HttpServletRequest request) {
        subjectService.removeTeachers(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @RequestMapping(value = "**/removeAllGroups", method = RequestMethod.POST)
    public String removeAllGroups(@RequestParam("id") Integer id, HttpServletRequest request) {
        programService.removeGroups(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    public void sendNew(Model model) {
        model.addAttribute("classroom", new Classroom());
        model.addAttribute("subject", new Subject());
        model.addAttribute("group", new Group());
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("exam", new Exam());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("groupo", groupService.findd());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("groupSubjectSearchFormSet", new GroupSubjectSearchFormSet());
        model.addAttribute("groupSearchForm", new GroupSearchForm());
    }

    private Comparator<Lesson> lessonComparator = new Comparator<Lesson>() {
        public int compare(Lesson lesson1, Lesson lesson2) {
            int cmp = lesson1.getLessonTime().getTimeValue() - lesson2.getLessonTime().getTimeValue();
            if (cmp != 0) {
                return cmp;
            }
            int dayCmp = lesson1.getDayOfWeek().getDayValue() - lesson2.getDayOfWeek().getDayValue();
            return dayCmp;
        }
    };
}