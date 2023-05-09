package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.models.User;
import web.services.UserService;

import javax.validation.Valid;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String usersPage(ModelMap model) {
        model.addAttribute("listUsers", userService.listUsers());
        return "index";
    }

    @GetMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        return "/add";
    }

    @PostMapping("/add")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/add";
        }
        userService.add(user);
        return "redirect:/";
    }

    @GetMapping("/find")
    public String findUser(@ModelAttribute("user") User user) {
        return "/find";
    }

    @PostMapping("/find")
    public String find(@ModelAttribute("user") User user) {
        Long id = userService.findByEmail(user.getEmail());
        System.out.println("Find id: " + id);

        return id > 0L ? "redirect:/" + id : "/find";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.findById(id));
        return "/edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("user", userService.findById(id));
        return "/user";
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult bindingResult
            , @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "/edit";
        }
        userService.update(id, user);
        return "redirect:/";
    }

    @DeleteMapping("/{id}/edit")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/";
    }

}
