package br.com.thyagodev.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository iUserRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = iUserRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            System.out.println("Username already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        var encryptedPass = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(encryptedPass);
        var userCreated = iUserRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
