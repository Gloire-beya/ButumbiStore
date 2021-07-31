package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistoreadmin.FileUploadUtil;
import com.butumbi.butumbistorecommon.entity.Role;
import com.butumbi.butumbistorecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {
       return listAllByPage(1, model, "firstName", "asc", null);
    }
    @GetMapping("/users/page/{pageNumber}")
    public String listAllByPage(@PathVariable int pageNumber, Model model,
                                @RequestParam String sortField,
                                @RequestParam String sortOrder,
                                @RequestParam(required = false, value= "keyword") String keyword){
        Page<User> userPage = userService.listByPage(pageNumber, sortField, sortOrder, keyword);

        List<User> userList = userPage.getContent();

        long startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
        long andCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (andCount > userPage.getTotalElements()){
            andCount = userPage.getTotalElements();
        }
        String reverseOrder = sortOrder.equals("asc")? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("andCount", andCount);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElement", userPage.getTotalElements());
        model.addAttribute("userList", userList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseOrder", reverseOrder);

        return "users";
    }

    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> roleList = userService.listAllRoles();

        model.addAttribute("roleList", roleList);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create new user");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, @RequestParam("image") MultipartFile multipartFile,
                           RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);
            User savedUser = userService.saveUser(user);

            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleadDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.saveUser(user);
        }
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");

        return redirectUrlToAffectedUser(user);
    }

    private String redirectUrlToAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortOrder=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(Model model,
                           @PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findUserById(id);
            List<Role> roleList = userService.listAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roleList", roleList);
            model.addAttribute("pageTitle", "Edit user (ID:" + id + ")");
            return "user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(RedirectAttributes redirectAttributes,
                             @PathVariable Integer id) {
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("message", "User has been deleted successfully");
            return "redirect:/users";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable Integer id,
                                       @PathVariable("status") boolean enabled,
                                       RedirectAttributes redirectAttributes){

        userService.updateEnableStatus(id,enabled);
        String status = enabled? "enabled" : "disable";
        String message = "The user with the ID "+ id + " has been "+ status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAllUsers();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.exporter(userList, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAllUsers();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.exporter(userList, response);
    }
    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAllUsers();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.exporter(userList, response);
    }
}
