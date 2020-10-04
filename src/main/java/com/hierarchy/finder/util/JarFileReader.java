package com.hierarchy.finder.util;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class JarFileReader {
    private List<String> classNames = null;

    public List<String> findListOfClassesFromJar(String jarLocation, String jarName) throws Exception{
        if(!isValidFile(jarLocation, jarName)){
            return classNames;
        }
        classNames = new ArrayList<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarLocation+"\\"+jarName+".jar"));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }
        return classNames;
    }

    private boolean isValidFile(String path, String name) {
        try {
            if(isValidPath(path))
                Paths.get(path+"/"+name);
        } catch (InvalidPathException | NullPointerException ex) {
            classNames = new ArrayList<>();
            classNames.add("Could not found file with provided path of "+(path+"/"+name));
            return false;
        }
        return true;
    }

    private boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            classNames = new ArrayList<>();
            classNames.add("Could not found file with provided path of "+path);
            return false;
        }
        return true;
    }
}
