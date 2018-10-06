package ru.chumarin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chumarin.configuration.ApplicationConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class FileHandlerImpl implements FileHandler {

    @Autowired
    ApplicationConfig config;

    private static Logger Log = LogManager.getLogger(FileHandlerImpl.class);

    @Override
    public List<String> findNumber(Integer number) throws FindNumberException {
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Path> pathFiles = getPathFiles(config.getFileDir());
        List<Callable<String>> callables = new ArrayList<>(pathFiles.size());
        List<String> collect = null;
        for (Path path : pathFiles) {
            callables.add(() -> findIntInFile(path.toString(), number));
        }
        try {
            collect = executor.invokeAll(callables)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            Log.error("File search error");
            throw new FindNumberException("File search error");
        }
        executor.shutdown();

        return collect;
    }

    private List<Path> getPathFiles(String dir){
        try {
            return Files.walk(Paths.get(dir)).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            Log.error("Error get path files");
        }
        return null;
    }

    private String findIntInFile(String pathName, int expectedNumber){
        Path file = Paths.get(pathName);
        try (BufferedReader in = Files.newBufferedReader(file)){
            StreamTokenizer reader = new StreamTokenizer(Files.newBufferedReader(file));
            while (reader.nextToken()!= StreamTokenizer.TT_EOF){
                int number = (int)reader.nval;
                if (expectedNumber == number) {
                    return file.getFileName().toString();
                }
                reader.nextToken();
            }
        } catch (IOException ex) {
            Log.error("Error find number in file %s", file.toString());
        }
        return null;
    }

}
