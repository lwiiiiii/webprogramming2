package net.skhu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.skhu.dto.Department;
import net.skhu.dto.Student;
import net.skhu.mapper.DepartmentMapper;
import net.skhu.mapper.RegisterMapper;
import net.skhu.mapper.StudentMapper;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired StudentMapper studentMapper;
    @Autowired DepartmentMapper departmentMapper;
    @Autowired RegisterMapper registerMapper;

    @RequestMapping(value="list", method=RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        List<Student> students = studentMapper.findAll();
        model.addAttribute("students", students);
        request.setAttribute("st", "");
        return "student/list";
    }

    @RequestMapping(value="list", method=RequestMethod.POST)
    public String list(Model model, HttpServletRequest request, @RequestParam("st") String st) {
        List<Student> students = studentMapper.findByName(st);
        model.addAttribute("students", students);
        request.setAttribute("st", st);
        return "student/list";
    }

    @RequestMapping(value="create", method=RequestMethod.GET)
    public String create(Model model) {
        Student student = new Student();
        List<Department> departments = departmentMapper.findAll();
        model.addAttribute("student", student);
        model.addAttribute("departments", departments);
        return "student/edit";
    }

    @RequestMapping(value="create", method=RequestMethod.POST)
    public String create(Model model, Student student) {
        studentMapper.insert(student);
        return "redirect:list";
    }

    @RequestMapping(value="edit", method=RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") int id) {
        Student student = studentMapper.findOne(id);
        List<Department> departments = departmentMapper.findAll();
        model.addAttribute("student", student);
        model.addAttribute("departments", departments);
        return "student/edit";
    }

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String edit(Model model, Student student) {
        studentMapper.update(student);
        return "redirect:list";
    }

    @RequestMapping("delete")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String delete(Model model, @RequestParam("id") int id) {
        registerMapper.deleteByStudentId(id);
        studentMapper.delete(id);
        return "redirect:list";
    }

}
