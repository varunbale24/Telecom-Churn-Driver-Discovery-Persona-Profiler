package com.telechurn.ai.repository;

import com.telechurn.ai.entity.Persona;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByName(String name);
}
