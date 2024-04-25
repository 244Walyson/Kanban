package com.waly.kanban.controllers;

import com.waly.kanban.configs.AuthorizationServerConfig;
import com.waly.kanban.dto.UserDTO;
import com.waly.kanban.dto.UserInsertDTO;
import com.waly.kanban.dto.UserLoggedDTO;
import com.waly.kanban.dto.UserUpdateDTO;
import com.waly.kanban.services.TokenService;
import com.waly.kanban.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService service;
    @Autowired
    private AuthorizationServerConfig authorizationServer;
    @Autowired
    private TokenService tokenGenerator;

    @GetMapping("/me")
    public ResponseEntity<UserLoggedDTO> getMe(){
        return ResponseEntity.ok(service.getMe());
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
        return ResponseEntity.ok(service.insert(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/ok")
    public ResponseEntity<String> getToken(Authentication authentication){
        return ResponseEntity.ok(tokenGenerator.getToken(authentication));
    }
}
