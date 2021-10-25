package io.acemouty.springjwt.api;

import io.acemouty.springjwt.clientmapping.RoleToUser;
import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.models.Role;
import io.acemouty.springjwt.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

  @PostMapping(path = "/user/save", produces = "application/json")
  public ResponseEntity<ApplicationUser> createUser(@RequestBody ApplicationUser user)
  {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
    return ResponseEntity.created(uri).body(userService.saveUser(user));
  }

  @PostMapping(path = "/role/save", produces = "application/json")
  public ResponseEntity<Role> createRole(@RequestBody Role role)
  {
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
    return ResponseEntity.created(uri).body(userService.saveRole(role));
  }

  @PostMapping(path = "/role/addUserRole", produces = "application/json")
  public ResponseEntity<?> addUserRole(@RequestBody RoleToUser payload)
  {
    userService.addUserRole(payload.getUsername(), payload.getRolename());
    return ResponseEntity.ok().build();
  }
}
