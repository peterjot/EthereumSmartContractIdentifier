package io.github.peterjot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginController {

    @GetMapping("/login")
    String showLoginPage() {
        return "login";
    }
}
