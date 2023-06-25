package com.iffomko.apsofttesttask.controllers;

import com.iffomko.apsofttesttask.services.FilesLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер, который обслуживает файлы
 */
@RestController
@RequestMapping("api/v1/files")
public class FileLoaderController {
    private final FilesLoaderService filesLoaderService;

    /**
     * @param filesLoaderService класс-сервис, который обслуживает этот контроллер
     */
    @Autowired
    public FileLoaderController(FilesLoaderService filesLoaderService) {
        this.filesLoaderService = filesLoaderService;
    }

    /**
     * Endpoint, который принимает текстовый файл и парсит его в html формат,
     * где сначала идет содержание текст (раздел помечается определяющим признаком в начале строки
     * исходного текста), а затем сам текст
     * @param textFile текстовый файл полученный из сети
     */
    @PostMapping(
            path = "/html-parser",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> parseFile(@RequestParam("file") MultipartFile textFile) {
        return filesLoaderService.parseFile(textFile);
    }
}
