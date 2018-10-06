package ru.chumarin.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.chumarin.database.entity.DbFilename;

/**
 * Репозиторий для работы с таблицей FILENAME
 */
@Repository
public interface FilenameRepository extends JpaRepository<DbFilename, Long> {

}