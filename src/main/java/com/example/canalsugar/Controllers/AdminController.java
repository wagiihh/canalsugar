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
import com.example.canalsugar.Models.Laptop;
import com.example.canalsugar.Models.User;
import com.example.canalsugar.Repositories.AdminRepository;
import com.example.canalsugar.Repositories.DepartmentRepository;
import com.example.canalsugar.Repositories.LaptopRepository;
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
    @Autowired
    private LaptopRepository laptopRepository;

    @GetMapping("/home")
    public ModelAndView Home(HttpSession session) {
        String firstname = (String) session.getAttribute("Firstname");
        System.out.println("Firstname from session: " + firstname);
        ModelAndView mav = new ModelAndView("CSHome");
        mav.addObject("FNAME", firstname);
        return mav;
    }

    @GetMapping("/settings")
    public ModelAndView settings(HttpSession session) {
        String firstname = (String) session.getAttribute("Firstname");

        ModelAndView mav = new ModelAndView("AccountSettings");
        mav.addObject("email", (String) session.getAttribute("email"));
        mav.addObject("FNAME", firstname);
        return mav;
    }

    @GetMapping("Profile")
    public ModelAndView getProfile(HttpSession session) {
        ModelAndView mav = new ModelAndView("Profile");

        mav.addObject("email", (String) session.getAttribute("email"));
        mav.addObject("firstname", (String) session.getAttribute("Firstname"));
        mav.addObject("lastname", (String) session.getAttribute("Lastname"));
        mav.addObject("number", (String) session.getAttribute("number"));
        return mav;
    }

    @GetMapping("editprofile")
    public ModelAndView getEditProfile(HttpSession session) {
        ModelAndView mav = new ModelAndView("EditProfile");

        mav.addObject("email", (String) session.getAttribute("email"));
        mav.addObject("firstname", (String) session.getAttribute("Firstname"));
        mav.addObject("lastname", (String) session.getAttribute("Lastname"));
        mav.addObject("number", (String) session.getAttribute("number"));
        mav.addObject("adminID", (Integer) session.getAttribute("adminID"));

        return mav;
    }

    @PostMapping("/editprofile")
    public RedirectView editprocess(HttpSession session,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("number") String number) {
        Admin adminedit = this.adminRepository.findByEmail((String) session.getAttribute("email"));
        if (adminedit != null) {
            session.setAttribute("Firstname", firstname);
            session.setAttribute("Lastname", lastname);
            session.setAttribute("number", number);

            adminedit.setFirstname(firstname);
            adminedit.setLastname(lastname);
            adminedit.setNumber(number);
            this.adminRepository.save(adminedit);

            return new RedirectView("/admin/home");
        }
        return new RedirectView("/admin/editprofile?error=incorrectForm");

    }

    @GetMapping("/deleteAccount")
    public RedirectView deleteAccount(HttpSession session) {
        Admin adminDelete = this.adminRepository.findByEmail((String) session.getAttribute("email"));
        if (adminDelete != null) {
            this.adminRepository.delete(adminDelete);
            session.invalidate();
            return new RedirectView("/");
        }
        return new RedirectView("/admin/Profile");
    }

    @GetMapping("/adduser")
    public ModelAndView showSignupForm() {
        ModelAndView mav = new ModelAndView("signup.html");
        User newUser = new User();
        mav.addObject("newUser", newUser);
        Department department = new Department();
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

    @GetMapping("/addlaptop")
    public ModelAndView showLaptopSignupForm() {
        ModelAndView mav = new ModelAndView("addLaptop");
        Laptop newLaptop = new Laptop();
        mav.addObject("newLaptop", newLaptop);
        return mav;
    }

    @PostMapping("addlaptop")
    public ModelAndView processLaptopSignupForm(@Valid @ModelAttribute("newLaptop") Laptop newLaptop,
            BindingResult result) {
        ModelAndView SignupModel = new ModelAndView("addLaptop");
        ModelAndView refresh = new ModelAndView("CSHOME.html");

        Laptop existingLaptop = laptopRepository.findBylaptopserial(newLaptop.getLaptopserial());

        if (existingLaptop != null) {
            return SignupModel;
        } else {
            Laptop laptop = newLaptop.getLaptop();
            this.laptopRepository.save(laptop);
            return new ModelAndView("redirect:/admin/home");

        }
    }

    @GetMapping("/viewlaptops")
    public ModelAndView showAddLaptopForm() {
        ModelAndView mav = new ModelAndView("viewLaptops");

        Laptop newLaptop = new Laptop();

        List<Laptop> allLaptops = laptopRepository.findAll();
        mav.addObject("allLaptops", allLaptops);

        return mav;
    }

    @GetMapping("editlaptops/{laptopid}")
    public ModelAndView editLaptopForm(@PathVariable Integer laptopid, HttpSession session) {
        ModelAndView mav = new ModelAndView("editLaptop");
        Laptop oldLaptop = this.laptopRepository.findBylaptopid(laptopid);
        System.out.println("-------------------------------------the user sent in the edit form :" + laptopid);
        mav.addObject("oldLaptop", oldLaptop);
        return mav;
    }

    @PostMapping("editlaptops/{laptopid}")
    public RedirectView updateLaptop(@ModelAttribute("oldLaptop") Laptop oldLaptop, @PathVariable Integer laptopid) {
        oldLaptop.setLaptopid(laptopid);
        this.laptopRepository.save(oldLaptop);
        return new RedirectView("/admin/viewlaptops");
    }

    @GetMapping("deletelaptops/{laptopid}")
    @Transactional
    public RedirectView deleteLaptop(@PathVariable Integer laptopid) {
        Laptop currLaptop = this.laptopRepository.findBylaptopid(laptopid);
        this.laptopRepository.delete(currLaptop);

        return new RedirectView("/admin/viewlaptops");
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
                session.setAttribute("number", newUser.getNumber());
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
