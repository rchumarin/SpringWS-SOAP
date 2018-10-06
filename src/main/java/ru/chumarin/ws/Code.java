package ru.chumarin.ws;

/**
 *  Код выполнения программы
 */
public enum Code {
    OK("00.Result.OK"),
    NOT_FOUND("01.Result.NotFound"),
    ERROR("02.Result.Error");

    private String description;

    public String getDescription() {
        return description;
    }

    Code(String description) {
        this.description = description;
    }


}
