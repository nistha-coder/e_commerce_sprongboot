package e_commerce.e_commerce.controller;

import e_commerce.e_commerce.model.User;
import e_commerce.e_commerce.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getById(id);
    }
}
