package com.hierarchy.finder.controller;

import com.hierarchy.finder.manager.JarReaderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/api/jar")
public class JavaFileSearchController {

    private ResponseEntity<List<String>> responseEntity = null;

    @Autowired
    private JarReaderManager jarReaderManager;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/class/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllClassNames(@NonNull @RequestParam String path,
                                                         @NotNull @RequestParam String jarName) throws Exception {
        List<String> response = jarReaderManager.getAllClassNamesFromJar(path, jarName);
        if (response != null)
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

}
