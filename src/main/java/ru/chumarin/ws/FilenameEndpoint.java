package ru.chumarin.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.chumarin.database.dto.FilenameDTO;
import ru.chumarin.database.repository.service.FilenameService;
import ru.chumarin.util.FileHandler;
import ru.chumarin.util.FindNumberException;
import ru.chumarin.ws.findnumber.GetFilenameRequest;
import ru.chumarin.ws.findnumber.GetFilenameResponse;
import ru.chumarin.ws.findnumber.Result;

import java.util.List;

/**
 * Класс для обработки входящих SOAP сообщений
 */
@Endpoint
public class FilenameEndpoint {
    private static final String NAMESPACE_URI = "http://chumarin.ru/ws/findNumber";

    private FileHandler fileHandler;
    private FilenameService repository;


    @Autowired
    public FilenameEndpoint(FileHandler fileHandler, FilenameService repository) {
        this.fileHandler = fileHandler;
        this.repository = repository;
    }

    /**
     * Выбор метода обработчика на основе namespace и localPart сообщения
     * @return GetFilenameResponse
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFilenameRequest")
    @ResponsePayload
    public GetFilenameResponse findNumber(@RequestPayload GetFilenameRequest request) {
        GetFilenameResponse response = new GetFilenameResponse();
        Result result = new Result();
        try {
            List<String> number = fileHandler.findNumber(request.getNumber());
            if (number == null || number.size() == 0) {
                result.setCode(Code.NOT_FOUND.getDescription());
            } else {
                result.getFilenames().addAll(number);
                result.setCode(Code.OK.getDescription());
            }
        } catch (FindNumberException e) {
            result.setCode(Code.ERROR.getDescription());
            result.setError(e.getMessage());
        }

        save(request.getNumber(), result);

        response.setResult(result);

        return response;
    }

    private void save(int number, Result result) {
        FilenameDTO filenameDTO = new FilenameDTO();
        filenameDTO.setCode(result.getCode());
        filenameDTO.setNumber(number);
        filenameDTO.setFilenames(result.getFilenames());
        filenameDTO.setError(result.getError());
        repository.save(filenameDTO);
    }

}