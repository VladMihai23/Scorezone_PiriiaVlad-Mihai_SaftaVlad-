package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.model.User;
import com.scorezone.scorezone.security.CustomUserDetails;
import com.scorezone.scorezone.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/users/login?from=profile";
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getUser().getId();

        model.addAttribute("user", userService.getUserById(userId).orElseThrow());
        return "profile";
    }


    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("email_taken".equals(error)) {
            model.addAttribute("errorMessage", "Email already registered.");
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        try {

            userService.registerUser(name, email, password);
            return "redirect:/users/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/users/register?error=email_taken";
        }
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam Long id, HttpSession session) {
        userService.deleteUser(id);
        session.invalidate();
        return "redirect:/users/register";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam(required = false) String newPassword) {
        userService.updateProfile(id, name, newPassword);
        return "redirect:/users/profile";
    }

    @PostMapping("/privacy")
    public String updatePrivacy(@RequestParam(value = "enabled", required = false) Boolean enabled,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        boolean isEnabled = (enabled != null && enabled);
        userService.updatePrivacy(userDetails.getUser().getId(), isEnabled);
        redirectAttributes.addFlashAttribute("successMessage", "Privacy settings updated.");
        return "redirect:/users/profile";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/users/reset-password";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No account found with that email.");
            return "redirect:/users/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@ModelAttribute("email") String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String email,
                                      @RequestParam String newPassword,
                                      RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            userService.changePassword(userOpt.get().getId(), newPassword);
            redirectAttributes.addFlashAttribute("message", "Password successfully reset. You can now log in.");
            return "redirect:/users/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid reset attempt.");
            return "redirect:/users/forgot-password";
        }
    }




}
