package io.acemouty.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role
{
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;
}
