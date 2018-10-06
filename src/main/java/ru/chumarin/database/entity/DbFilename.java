package ru.chumarin.database.entity;

import javax.persistence.*;

/**
 * Сущность содержащая информацию о найденных числах и файлах
 */
@Entity
@Table(name = "filename", indexes = {
        @Index(name = "idx_filenames", columnList = "filenames", unique = true),
        @Index(name = "idx_num", columnList = "num", unique = true)
})
public class DbFilename {

    public final static int SEQUENCE_ALLOCATION_SIZE = 1;

    @Id
    @GeneratedValue(generator = "filename_seq")
    @SequenceGenerator(name = "filename_seq", sequenceName = "filename_seq", allocationSize = SEQUENCE_ALLOCATION_SIZE)
    private Long id;

    /**
     * Код выполнения программы
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Число, переданное на вход
     */
    @Column(name = "num", nullable = false)
    private Integer number;

    /**
     * Имена файлов, в которых удалось найти число
     */
    @Column(name = "filenames")
    private String filenames;

    /**
     * Описание ошибки, в случае её возникновения
     */
    @Column(name = "error")
    private String error;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFilenames() {
        return filenames;
    }

    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}