package com.hierarchy.finder.manager;

import com.hierarchy.finder.util.JarFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JarReaderManager {

    @Autowired
    private JarFileReader jarFileReader;

    public List<String> getAllClassNamesFromJar(String path, String jarFileName) throws Exception {
        return jarFileReader.findListOfClassesFromJar(path, jarFileName);
    }
}
