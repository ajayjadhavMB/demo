package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FileAsset;

public interface FileAssetRepository extends JpaRepository<FileAsset, Long> {
    List<FileAsset> findAllByOrderByCreatedAtDesc();
}
