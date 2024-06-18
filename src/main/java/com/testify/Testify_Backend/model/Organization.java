package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User admin;

    @OneToMany(mappedBy = "Organization", cascade = CascadeType.ALL)
    private Set<User> users;
}
