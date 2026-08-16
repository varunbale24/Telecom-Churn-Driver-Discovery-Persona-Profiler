package com.telechurn.ai.repository;

import com.telechurn.ai.entity.ModelVersion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelVersionRepository extends JpaRepository<ModelVersion, Long> {
    Optional<ModelVersion> findTopByModelTypeOrderByTrainedAtDesc(String modelType);
}
