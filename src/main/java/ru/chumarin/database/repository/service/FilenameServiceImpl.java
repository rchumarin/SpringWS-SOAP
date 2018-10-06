package ru.chumarin.database.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.chumarin.database.dto.FilenameDTO;
import ru.chumarin.database.entity.DbFilename;
import ru.chumarin.database.repository.FilenameRepository;

/**
 * Сервис для сохранения сущности DbFilename в БД
 */
@Component
class FilenameServiceImpl implements FilenameService {

    private FilenameRepository repository;

    @Autowired
    public FilenameServiceImpl(FilenameRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(FilenameDTO filenameDTO) {
        repository.save(createFilenameEntity(filenameDTO));
    }

    private DbFilename createFilenameEntity(FilenameDTO filenameDTO) {
        DbFilename dbFilename = new DbFilename();
        dbFilename.setCode(filenameDTO.getCode());
        dbFilename.setFilenames(filenameDTO.getFilenames());
        dbFilename.setNumber(filenameDTO.getNumber());
        dbFilename.setError(filenameDTO.getError());

        return dbFilename;
    }

}
