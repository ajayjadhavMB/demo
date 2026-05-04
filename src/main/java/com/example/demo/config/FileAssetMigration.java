package com.example.demo.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.service.FileStorageService;

@Component
public class FileAssetMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FileAssetMigration.class);

    private final JdbcTemplate jdbcTemplate;
    private final FileStorageService fileStorageService;

    public FileAssetMigration(JdbcTemplate jdbcTemplate, FileStorageService fileStorageService) {
        this.jdbcTemplate = jdbcTemplate;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!legacyBlobColumnExists()) {
            return;
        }

        List<LegacyFileRow> legacyRows = jdbcTemplate.query(
                """
                        select id, original_name, content_type, file_size, file_data
                        from file_assets
                        where file_data is not null
                        order by id
                        """,
                (rs, rowNum) -> new LegacyFileRow(
                        rs.getLong("id"),
                        rs.getString("original_name"),
                        rs.getString("content_type"),
                        rs.getLong("file_size"),
                        rs.getBytes("file_data"))
        );

        for (LegacyFileRow row : legacyRows) {
            if (row.fileData() == null || row.fileData().length == 0) {
                continue;
            }

            String objectKey = fileStorageService.storeInS3(row.originalName(), row.contentType(), row.fileData());
            jdbcTemplate.update("update file_assets set object_key = ? where id = ?", objectKey, row.id());
        }

        jdbcTemplate.execute("alter table if exists file_assets drop column if exists file_data");
        log.info("Migrated {} legacy file rows to S3 and dropped file_data column", legacyRows.size());
    }

    private boolean legacyBlobColumnExists() {
        Integer count = jdbcTemplate.queryForObject(
                """
                        select count(*)
                        from information_schema.columns
                        where table_name = 'file_assets'
                          and column_name = 'file_data'
                        """,
                Integer.class);
        return count != null && count > 0;
    }

    private record LegacyFileRow(Long id, String originalName, String contentType, Long fileSize, byte[] fileData) {
    }
}
