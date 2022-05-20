package ua.moskovych.taras.controllers;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.moskovych.taras.entity.*;
import ua.moskovych.taras.services.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Taras on 03.05.2017.
 */

@Controller
public class RestController {
    @Autowired
    private ExamService anas ;
    @Autowired
    private ExamService pdfGeneratorService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ProgramService programService;

    @RequestMapping(value = "/timetable/groups/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Group> showAllGroups(){
        return groupService.findAll();
    }

    @GetMapping("/pdf/{teacherId}")
    public void generatePDF(HttpServletResponse response,@PathVariable("teacherId") Integer teacherId) throws IOException {


        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.export(response,teacherId);
    }

    @PostMapping("/anas/upload")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Subject> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Subject.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<Subject> users = csvToBean.parse();

                // TODO: save users in DB?

                // save users list on model
                model.addAttribute("users", users);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }


    @RequestMapping(value = "/timetable/groups/add", method = RequestMethod.POST)
    @ResponseBody
    public void addNewGroup(@RequestBody Group group){
        groupService.add(group);
    }

    @RequestMapping(value = "/timetable/groups/edit", method = RequestMethod.POST)
    @ResponseBody
    public void editGroup(@RequestBody Group group) {
        groupService.edit(group.getId(), group.getName(), group.getNumberOfStudents());
    }

    @RequestMapping(value = "/timetable/groups/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteGroup(@RequestBody Integer id) {
        List<Lesson> lessons = lessonService.findByGroupId(id);
        groupService.delete(id);
        for (Lesson lesson : lessons) {
            lessonService.delete(lesson.getId());
        }
    }
    @RequestMapping(value = "/anass", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long,String> count (){

        Map<Long,String> map = new HashMap<Long,String>();
        for(Object[] obj : anas.findByT()){
            map.put((Long) obj[0], String.valueOf(obj[1]));
        }

        return map;

    }
    @RequestMapping(value = "/timetable/teachers/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Teacher> showAllTeachers(){
        return teacherService.findAll();
    }

    @RequestMapping(value = "/timetable/teachers/add", method = RequestMethod.POST)
    @ResponseBody
    public void addNewTeacher(@RequestBody Teacher teacher){
        teacherService.add(teacher);
    }

    @RequestMapping(value = "/timetable/teachers/edit", method = RequestMethod.POST)
    @ResponseBody
    public void editTeacher(@RequestBody Teacher teacher) {
        teacherService.edit(teacher.getId(), teacher.getName(), teacher.getSecondName(), teacher.getTeacherTitle(), teacher.getHours());
    }

    @RequestMapping(value = "/timetable/teachers/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteTeacher(@RequestBody Integer id) {
        teacherService.delete(id);
    }

    @RequestMapping(value = "/timetable/classrooms/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Classroom> showAllClassrooms(){
        return classroomService.findAll();
    }

    @RequestMapping(value = "/timetable/classrooms/add", method = RequestMethod.POST)
    @ResponseBody
    public void addNewClassroom(@RequestBody Classroom classroom){
        classroomService.add(classroom);
    }

    @RequestMapping(value = "/timetable/classrooms/edit", method = RequestMethod.POST)
    @ResponseBody
    public void editClassroom(@RequestBody Classroom classroom) {
        classroomService.edit(classroom);
    }

    @RequestMapping(value = "/timetable/classrooms/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteClassroom(@RequestBody Integer id) {
        classroomService.delete(id);
    }

    @RequestMapping(value = "/timetable/subjects/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Subject> showAllSubjects(){
        return subjectService.findAll();
    }

    @RequestMapping(value = "/timetable/subjects/add", method = RequestMethod.POST)
    @ResponseBody
    public void addNewSubject(@RequestBody Subject subject){
        subjectService.add(subject);
    }

    @RequestMapping(value = "/timetable/subjects/edit", method = RequestMethod.POST)
    @ResponseBody
    public void editSubject(@RequestBody Subject subject) {
        subjectService.edit(subject);
    }

    @RequestMapping(value = "/timetable/subjects/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteSubject(@RequestBody Integer id) {
        subjectService.delete(id);
    }

    @RequestMapping(value = "/timetable/curriculum/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Program> showAllPrograms(){
        return programService.findAll();
    }

    @RequestMapping(value = "/timetable/curriculum/add", method = RequestMethod.POST)
    @ResponseBody
    public void addProgram(@RequestBody Program program){
        programService.add(program);
    }

    @RequestMapping(value = "/timetable/curriculum/edit", method = RequestMethod.POST)
    @ResponseBody
    public void editProgram(@RequestBody Program program) {
        programService.edit(program.getId(), program.getName(), program.getHours());
    }

    @RequestMapping(value = "/timetable/curriculum/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteProgram(@RequestBody Integer id) {
        programService.delete(id);
    }
}