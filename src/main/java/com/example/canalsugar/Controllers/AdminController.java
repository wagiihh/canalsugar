package com.example.canalsugar.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import com.example.canalsugar.Models.AssignedLaptops;
import com.example.canalsugar.Models.Department;
import com.example.canalsugar.Models.Laptop;
import com.example.canalsugar.Models.User;
import com.example.canalsugar.Repositories.AdminRepository;
import com.example.canalsugar.Repositories.AssignedLaptopsRepository;
import com.example.canalsugar.Repositories.DepartmentRepository;
import com.example.canalsugar.Repositories.LaptopRepository;
import com.example.canalsugar.Repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
    @Autowired
    private AssignedLaptopsRepository assignedLaptopsRepository;
    @Autowired
    private JavaMailSenderImpl mailSender;

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
    @GetMapping("/stock")
    public ModelAndView showstockForm() {
        ModelAndView mav = new ModelAndView("stock");
        long totalLaptops = laptopRepository.count();
        long usedLaptops = assignedLaptopsRepository.count();

        mav.addObject("totalLaptops", totalLaptops);
        mav.addObject("usedLaptops", usedLaptops);
        mav.addObject("availableLaptops", totalLaptops - usedLaptops);

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

    @GetMapping("/assignLaptop")
    public ModelAndView showAssignLaptopForm(HttpSession session) {
        ModelAndView mav = new ModelAndView("AssignLaptops");

        List<Laptop> allLaptops = laptopRepository.findAll();
        List<Laptop> assignedLaptops = assignedLaptopsRepository.findAll().stream()
                .map(AssignedLaptops::getLaptop)
                .collect(Collectors.toList());

        List<Laptop> availableLaptops = allLaptops.stream()
                .filter(laptop -> !assignedLaptops.contains(laptop))
                .collect(Collectors.toList());

        mav.addObject("users", userRepository.findAll());
        mav.addObject("laptops", availableLaptops);
        mav.addObject("assignedLaptop", new AssignedLaptops());

        return mav;
    }

    @PostMapping("/assignLaptop")
    public ModelAndView assignLaptop(@ModelAttribute("assignedLaptop") AssignedLaptops assignedLaptop,
            ModelAndView mav,HttpSession session) {
        mav.setViewName("AssignLaptops");
                
        Laptop laptop = assignedLaptop.getLaptop();

        if (assignedLaptopsRepository.findByLaptop(laptop) != null) {
            mav.addObject("error", "This laptop is already assigned to another user.");
            mav.addObject("users", userRepository.findAll());
            mav.addObject("laptops", laptopRepository.findAll());
            return mav;
        }

        long totalLaptops = laptopRepository.count();
        long usedLaptops = assignedLaptopsRepository.count();
        long availableLaptops=totalLaptops-usedLaptops;
        if(availableLaptops<=3)
        {
            String emaill=(String) session.getAttribute("email");
            send_stock_warning(emaill);
        }
        try {
            assignedLaptopsRepository.save(assignedLaptop);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            mav.addObject("error", "An error occurred while assigning the laptop.");
            mav.addObject("users", userRepository.findAll());
            mav.addObject("laptops", laptopRepository.findAll());
            return mav;
        }

        return new ModelAndView("redirect:/admin/viewassignedlaptops");
    }


public void send_stock_warning(String mail)
{         // Set up the mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("tabibii.application@gmail.com");
        mailSender.setPassword("maga ltqn qnoi azhz");


        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        // Create a mime message
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("tabibii.application@gmail.com");
            helper.setTo(mail);
            helper.setSubject("CHECK YOUR STOCK");
            helper.setText("your stock of laptops is almost empty ");
            // Send the mail
            mailSender.send(message);
            System.out.println("Mail sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error while sending mail.");
        }

}


    @GetMapping("editassignedlaptops/{alID}")
public ModelAndView editAssignedForm(@PathVariable Integer alID, HttpSession session) {
    ModelAndView mav = new ModelAndView("editAssignedLaptops");
    AssignedLaptops oldAssigned = this.assignedLaptopsRepository.findByAlID(alID);

    if (oldAssigned.getLaptop() != null) {
        Laptop laptop = laptopRepository.findById(oldAssigned.getLaptop().getLaptopid())
                            .orElseThrow(() -> new IllegalArgumentException("Laptop not found"));
        oldAssigned.setLaptop(laptop);
    }

    List<User> users = userRepository.findAll();
    mav.addObject("oldAssigned", oldAssigned);
    mav.addObject("users", users);
    return mav;
}

    
@PostMapping("editassignedlaptops/{alID}")
public RedirectView updateAssigned(@ModelAttribute("oldAssigned") AssignedLaptops oldAssigned, @PathVariable Integer alID) {
    // Fetch the existing AssignedLaptops object from the database to ensure it's managed
    AssignedLaptops existingAssigned = assignedLaptopsRepository.findByAlID(alID);
    
    if (existingAssigned == null) {
        throw new IllegalArgumentException("Assigned Laptop not found");
    }

    // Fetch the User from the database based on the ID in oldAssigned
    User user = userRepository.findById(oldAssigned.getUser().getUserID())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    // Update only the modifiable fields
    existingAssigned.setUser(user);
    
    // Save the updated AssignedLaptops object
    this.assignedLaptopsRepository.save(existingAssigned);
    
    return new RedirectView("/admin/viewassignedlaptops");
}

    

    @GetMapping("deleteassignedlaptops/{alID}")
    @Transactional
    public RedirectView deleteAssigned(@PathVariable Integer alID) {
        AssignedLaptops assignedLaptops=this.assignedLaptopsRepository.findByAlID(alID);
        this.assignedLaptopsRepository.delete(assignedLaptops);

        return new RedirectView("/admin/viewassignedlaptops");
    }

    @GetMapping("/viewassignedlaptops")
    public ModelAndView showassigendForm() {
        ModelAndView mav = new ModelAndView("viewAssignedLaptops");

        AssignedLaptops assignedLaptops = new AssignedLaptops();

        List<AssignedLaptops> allAssignedLaptops = assignedLaptopsRepository.findAll();
        mav.addObject("allAssignedLaptops", allAssignedLaptops);

        return mav;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }
}
