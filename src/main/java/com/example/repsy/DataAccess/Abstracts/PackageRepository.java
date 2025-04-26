package com.example.repsy.DataAccess.Abstracts;

import com.example.repsy.Entities.Concretes.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByPackageNameAndVersion(String packageName, String version);
}
