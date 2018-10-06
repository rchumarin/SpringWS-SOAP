package ru.chumarin.util;

import java.util.List;

/**
 *  Поиск тестовых файлов
 */
public interface FileHandler {

    List<String> findNumber(Integer number) throws FindNumberException;

}
