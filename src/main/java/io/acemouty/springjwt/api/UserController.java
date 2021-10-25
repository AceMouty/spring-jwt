package io.acemouty.springjwt.api;

import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController
{
  private final UserServiceImpl userService;

  @GetMapping(path = "/users", produces = "application/json")
  public ResponseEntity<List<ApplicationUser>> getUsers()
  {
    return ResponseEntity.ok().body(userService.getAllUsers());
  }
}
