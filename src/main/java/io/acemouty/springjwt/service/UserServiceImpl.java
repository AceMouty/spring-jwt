package io.acemouty.springjwt.service;

import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.models.Role;
import io.acemouty.springjwt.repository.RoleRepo;
import io.acemouty.springjwt.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // we are altering / creating data, we need transactional annotation
@Slf4j // simple logging
public class UserServiceImpl implements ApplicationUserService
{
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;

  @Override
  public ApplicationUser saveUser(ApplicationUser user)
  {
    log.info("Saving user {}; To the DB", user.getName());
    return userRepo.save(user);
  }

  @Override
  public Role saveRole(Role role)
  {
    log.info("Saving role: {}; To the DB", role.getName());
    return roleRepo.save(role);
  }

  // TODO: Add sanity checking for values.
  @Override
  public void addUserRole(String username, String rolename)
  {
    ApplicationUser user = userRepo.findByUsername(username);
    Role role = roleRepo.findByName(rolename);

    log.info("Adding Role {} to User {}", rolename, username);
    user.getRoles().add(role);
  }

  @Override
  public ApplicationUser getUser(String username)
  {
    log.info("Fetching User {} from the DB", username);
    return userRepo.findByUsername(username);
  }

  @Override
  public List<ApplicationUser> getAllUsers()
  {
    log.info("Fetching all users");
    return userRepo.findAll();
  }
}
