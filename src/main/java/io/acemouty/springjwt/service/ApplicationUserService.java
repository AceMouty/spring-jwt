package io.acemouty.springjwt.service;

import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.models.Role;

import java.util.List;

public interface ApplicationUserRepo
{
  ApplicationUser saveUser(ApplicationUser user);
  Role saveRole(Role role);
  void addUserRole(String username, String rolename);
  ApplicationUser getUser(String username);
  List<ApplicationUser> getAllUsers();
}
