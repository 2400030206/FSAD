package com.ai.startup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.startup.model.User;
import com.ai.startup.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Autowired
UserRepository repo;

@PostMapping("/login")
public String login(@RequestBody User user){

if(user == null || user.getEmail() == null || user.getPassword() == null){
return "Email and password required";
}

String email = user.getEmail().trim();
String password = user.getPassword();

if(email.isEmpty() || password.isEmpty()){
return "Email and password required";
}

User db=repo.findByEmail(email);

if(db==null){
User created=new User();
created.setEmail(email);
created.setPassword(password);
repo.save(created);
return "Login success";
}

if(db.getPassword() != null && db.getPassword().equals(password))
return "Login success";

return "Invalid login";
}
}
