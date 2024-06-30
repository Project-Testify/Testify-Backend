package com.testify.Testify_Backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ESSAY")
public class Essay extends Question{
    @OneToMany(mappedBy = "essayQuestion", cascade = CascadeType.ALL)
    private Set<EssayCoverPoint> coverPoints;
}
