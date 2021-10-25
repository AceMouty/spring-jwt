package io.acemouty.springjwt;

import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.models.Role;
import io.acemouty.springjwt.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Component
@Transactional
public class SeedData
    implements CommandLineRunner
{
  @Autowired
  ApplicationUserService userService;

  @Transactional
  @Override
  public void run(String[] args)
    throws Exception
  {
    userService.saveRole(new Role(null, "ROLE_USER"));
    userService.saveRole(new Role(null, "ROLE_MANAGER"));
    userService.saveRole(new Role(null, "ROLE_ADMIN"));
    userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

    userService.saveUser(new ApplicationUser(null, "John Travolta", "john", "1234", new ArrayList<>()));
    userService.saveUser(new ApplicationUser(null, "Will Smith", "will", "1234", new ArrayList<>()));
    userService.saveUser(new ApplicationUser(null, "Jim Carry", "jim", "1234", new ArrayList<>()));
    userService.saveUser(new ApplicationUser(null, "Arnold Schwarzenegger", "arnold", "1234", new ArrayList<>()));
    
    userService.addUserRole("john", "ROLE_USER");
    userService.addUserRole("will", "ROLE_MANAGER");
    userService.addUserRole("jim", "ROLE_ADMIN");
    userService.addUserRole("arnold", "ROLE_SUPER_ADMIN");
    userService.addUserRole("arnold", "ROLE_ADMIN");
    userService.addUserRole("arnold", "ROLE_USER");
  }
}
