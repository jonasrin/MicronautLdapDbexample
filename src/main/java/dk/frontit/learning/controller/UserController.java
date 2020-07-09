package dk.frontit.learning.controller;

import dk.frontit.learning.domain.User;
import dk.frontit.learning.repository.UserRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Controller("/user")
public class UserController {


    protected final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Get
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }
}
