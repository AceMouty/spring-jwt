package io.acemouty.springjwt.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.acemouty.springjwt.clientmapping.RoleToUser;
import io.acemouty.springjwt.models.ApplicationUser;
import io.acemouty.springjwt.models.Role;
import io.acemouty.springjwt.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

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

  @GetMapping(path = "/auth/refresh", produces = "applicaiton/json")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
          throws IOException
  {
    String authorization = request.getHeader(AUTHORIZATION);
    if(authorization != null && authorization.startsWith("Bearer "))
    {
      try
      {
        // token is refesh token here
        String token = authorization.substring("Bearer ".length());
        Algorithm algo = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algo).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        ApplicationUser user = userService.getUser(username);
        // TODO: Create a helper for creating a token and refresh token
        // create a new token and refresh token for the user
        Collection<SimpleGrantedAuthority> authorityCollection = new ArrayList<>();
        user.getRoles().forEach(role -> {
          authorityCollection.add(new SimpleGrantedAuthority(role.getName()));
        });

        List<String> userRoles = authorityCollection.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", userRoles)
                .sign(algo);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 min timeout
                .withIssuer(request.getRequestURL().toString())
                .sign(algo);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
      }
      catch (Exception e)
      {
        response.setHeader("error", "Error: Please provide a valid token");
        response.setStatus(FORBIDDEN.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();
        error.put("error_message", "Error: Please provide a valid token");
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    }
    else
    {
      throw new RuntimeException("Unable to refresh access token");
    }
  }
}
