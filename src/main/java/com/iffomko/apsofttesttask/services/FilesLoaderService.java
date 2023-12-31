package com.iffomko.apsofttesttask.services;

import com.iffomko.apsofttesttask.services.responses.FilesLoaderErrorResponse;
import com.iffomko.apsofttesttask.services.responses.FilesLoaderResponse;
import com.iffomko.apsofttesttask.services.responses.utils.FileLoaderResponseCodes;
import com.iffomko.apsofttesttask.services.parser.IFileParser;
import com.iffomko.apsofttesttask.services.responses.utils.FileLoaderResponseMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервис с бизнес-логикой по обработке загружаемых файлов.
 */
@Service
@Slf4j
public class FilesLoaderService {
    private final IFileParser fileParser;
    private final Charset charset;

    /**
     * @param fileParser парсер файлов
     */
    @Autowired
    public FilesLoaderService(@Qualifier("intoHtmlFileParser") IFileParser fileParser) {
        this.fileParser = fileParser;
        this.charset = StandardCharsets.UTF_8;
    }

    /**
     * Разбивает строку на строчки по CRLF (либо CR, либо LF, либо то и то)
     * @param bytes строчка в байтах
     * @return список строчек
     */
    private List<String> getLines(byte[] bytes) {
        String text = new String(bytes, charset);

        return Arrays.stream(text.split("\r|\n|\r\n")).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Переводит стек вызовов в строковое представление
     * @param stackTraceElements сам стек вызовов
     * @return строковое представление
     */
    private String stackTraceElementsToString(StackTraceElement[] stackTraceElements) {
        StringBuilder stringView = new StringBuilder();

        for (StackTraceElement stackTraceElement : stackTraceElements) {
            stringView.append(stackTraceElement);
            stringView.append("\r\n");
        }

        return stringView.toString();
    }

    /**
     * <p>
     *     Парсит текстовый файл из формата, где символ '#' указывает на начало раздела,
     *     а количество повторений этого символа для раздела указывает на его вложенность,
     *     в формат HTML, где сначала идет структура разделов, посредством которой
     *     можно осуществлять навигацию по разделам, а затем сам текст.
     * </p>
     * @param multipartFile файл полученный из сети
     */
    public ResponseEntity<?> parseFile(MultipartFile multipartFile) {
        try {
            if (!(Objects.equals(multipartFile.getContentType(), MediaType.TEXT_PLAIN_VALUE))) {
                log.error(String.format(
                        "Invalid content-type in the request: %s",
                        multipartFile.getContentType()
                ));
                return ResponseEntity.badRequest().body(new FilesLoaderErrorResponse(
                        FileLoaderResponseMessages.INCORRECT_REQUEST_TYPE.getMessage(),
                        FileLoaderResponseCodes.INCORRECT_REQUEST_TYPE.name()
                ));
            }

            List<String> lines = getLines(multipartFile.getBytes());

            String resultText = this.fileParser.parse(lines);

            return ResponseEntity.ok(new FilesLoaderResponse(
                    FileLoaderResponseCodes.SUCCESS.name(),
                    resultText
            ));
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("Unsupported encoding exception: %s", e.getMessage()));
            return ResponseEntity.badRequest().body(new FilesLoaderErrorResponse(
                    FileLoaderResponseMessages.INCORRECT_ENCODING.getMessage(),
                    FileLoaderResponseCodes.INCORRECT_ENCODING.name()
            ));
        } catch (Exception e) {
            log.error(String.format(
                    "Internal server error:\r\nmessage: %s\r\nstack trace: %s",
                    e.getMessage(),
                    stackTraceElementsToString(e.getStackTrace())
            ));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FilesLoaderErrorResponse(
                    FileLoaderResponseMessages.INTERNAL_SERVER_ERROR.getMessage(),
                    FileLoaderResponseCodes.INTERNAL_SERVER_ERROR.name()
            ));
        }
    }
}
