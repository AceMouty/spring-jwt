package io.acemouty.springjwt.repository;

import io.acemouty.springjwt.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<ApplicationUser, Long>
{
  ApplicationUser findByUsername(String username);
}
