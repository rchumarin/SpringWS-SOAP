package ru.chumarin.database.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO содержащая информацию о найденных числах и файлах
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilenameDTO {

    private Long id;

    /**
     * Код выполнения программы
     */
    private String code;

    /**
     * Число, переданное на вход
     */
    private Integer number;

    /**
     * Имена файлов, в которых удалось найти число
     */
    private String filenames;

    /**
     * Описание ошибки, в случае её возникновения
     */
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

    public void setFilenames(List<String> filenames) {
        this.filenames = convertListToString(filenames);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String convertListToString(List<String> list) {
        if (filenames == null || filenames.isEmpty()) {
            return "";
        }
        return String.join(",", filenames);
    }

}