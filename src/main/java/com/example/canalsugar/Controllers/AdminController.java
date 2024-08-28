package com.example.canalsugar.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.canalsugar.Models.Admin;
import com.example.canalsugar.Models.Department;
import com.example.canalsugar.Models.User;
import com.example.canalsugar.Repositories.AdminRepository;
import com.example.canalsugar.Repositories.DepartmentRepository;
import com.example.canalsugar.Repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/home")
    public ModelAndView Home(HttpSession session) {
        String firstname = (String) session.getAttribute("Firstname");
        System.out.println("Firstname from session: " + firstname);
        ModelAndView mav = new ModelAndView("CSHome");
        mav.addObject("FNAME", firstname);
        return mav;
    }

    @GetMapping("/adduser")
    public ModelAndView showSignupForm() {
        ModelAndView mav = new ModelAndView("signup.html");
        User newUser = new User();
        mav.addObject("newUser", newUser);
        Department department=new Department();
        List<Department> departments = departmentRepository.findAll();
        mav.addObject("departments", departments);
        return mav;
    }

    @PostMapping("adduser")
    public ModelAndView processSignupForm(@Valid @ModelAttribute("newUser") User newUser, BindingResult result) {
        ModelAndView SignupModel = new ModelAndView("adduser.html");
        ModelAndView refresh = new ModelAndView("CSHOME.html");

        User existingUser = userRepository.findByEmail(newUser.getEmail());

        if (existingUser != null) {
            return SignupModel;
        } else {
            User user = newUser.getUser();
            this.userRepository.save(user);
            return new ModelAndView("redirect:/admin/home");

        }
    }

    @GetMapping("/viewUsers")
    public ModelAndView showAddUserForm() {
        ModelAndView mav = new ModelAndView("viewUsers.html");

        User newUser = new User();

        List<User> allUsers = userRepository.findAll();
        mav.addObject("allUsers", allUsers);

        return mav;
    }

    @GetMapping("edit/{userID}")
    public ModelAndView editAppointmentForm(@PathVariable Integer userID, HttpSession session) {
        ModelAndView mav = new ModelAndView("editUser.html");
        User oldUser = this.userRepository.findByUserID(userID);
        System.out.println("-------------------------------------the user sent in the edit form :" + oldUser);
        mav.addObject("oldUser", oldUser);
        List<Department> departments = departmentRepository.findAll();
        mav.addObject("departments", departments);
        return mav;
    }

    @PostMapping("edit/{userID}")
    public RedirectView updateAppointment(@ModelAttribute("oldUser") User oldUser, @PathVariable Integer userID) {
        oldUser.setUserID(userID);
        this.userRepository.save(oldUser);
        return new RedirectView("/admin/viewUsers");
    }
@GetMapping("delete/{userID}")
@Transactional
public RedirectView deleteAppointment(@PathVariable Integer userID) {
    User currUser = this.userRepository.findByUserID(userID);
    this.userRepository.delete(currUser);

    return new RedirectView("/admin/viewUsers");
}

    @GetMapping("/Login")
    public ModelAndView Login() {
        ModelAndView mav = new ModelAndView("Login.html");
        return mav;
    }

    @PostMapping("/Login")
    public RedirectView loginprocess(@RequestParam("email") String email, @RequestParam("pass") String pass,
            HttpSession session) {
        Admin newUser = this.adminRepository.findByEmail(email);

        if (newUser != null) {
            Boolean PasswordsMatch = BCrypt.checkpw(pass, newUser.getPass());
            PasswordsMatch = true;
            if (PasswordsMatch) {
                session.setAttribute("email", newUser.getEmail());
                session.setAttribute("adminID", newUser.getAdminID());
                session.setAttribute("Firstname", newUser.getFirstname());
                session.setAttribute("Lastname", newUser.getLastname());
                return new RedirectView("/admin/home");

            } else {
                return new RedirectView("/User/Login?error=incorrectPassword  " + email);
            }
        }
        return new RedirectView("/User/Login?error=userNotFound  " + email);
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }
}
