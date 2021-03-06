package com.hierarchy.finder.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.hierarchy.finder.scan.ScanCodeBase;

@Service
public class HierarchyManager {

	static final String basePackage = "\\src\\main\\java\\";

	@Autowired
	private Environment env;

	@Autowired
	private ScanCodeBase scanCodeBase;

	static List<String> filesList = null;

	public Map<String, List<String>> getHierarchy() throws FileNotFoundException, UnsupportedEncodingException {
		PrintStream pStream = new PrintStream("./output.txt", "UTF-8");
		System.setOut(pStream);
		long startTime = System.currentTimeMillis();
		List<File> totalFiles = getTotalFilesInGivenPath();

		Map<String, List<String>> dependencyMap = scanCodeBase.codeScanner(totalFiles, filesList);
		for (String name : dependencyMap.keySet()) {
			List<String> dependentFile = dependencyMap.get(name);
			int i = 0;
			if(dependentFile.size() != 1) {
				for (String fileName : dependentFile) {
					if (i == 0) {
						System.out.println(name + " " + fileName);
						i++;
					} else {
						System.out.println("\t\t\t" + fileName);
					}
				}
			}
		}
		System.out.println("Time taken to complete the task: " + ((System.currentTimeMillis() - startTime) / 1000));
		pStream.close();
		System.out
				.println("Process Completed........ Please find the results in output.txt file under the project root");
		return dependencyMap;
	}
	
	public List<String> findHierarchyOfString(String input) throws FileNotFoundException, UnsupportedEncodingException{
		PrintStream pStream = new PrintStream("./output.txt", "UTF-8");
		System.setOut(pStream);
		long startTime = System.currentTimeMillis();
		List<File> totalFiles = getTotalFilesInGivenPath();
		
		List<String> output = scanCodeBase.searchForString(input, totalFiles);
		
		System.out.println("Total time taken to complete: " + ((System.currentTimeMillis() - startTime) / 1000));
		pStream.close();
		System.out
				.println("Process Completed........ Please find the results in output.txt file under the project root");
		return output;
	}
	
	private List<File> getTotalFilesInGivenPath(){
		String path = env.getProperty("project.root.path");
		List<File> subdirs = findAllSubdirs(new File(path));
		subdirs.stream().filter(dir -> dir.getAbsolutePath().contains(basePackage)).forEach(dir -> System.out.println(dir.getAbsolutePath().toString()));
		filesList = findInputFilesToSearch(path);
		List<File> dirsToAnalyze = subdirs.parallelStream().filter(dir -> dir.getAbsolutePath().contains(basePackage))
				.collect(Collectors.toList());
		System.out.println("++++++++++++++++++ Total Count of Packages: " + dirsToAnalyze.size());

		List<File> totalFiles = totalFilesInDirectories(dirsToAnalyze);
		System.out.println("++++++++++++++++++ Total files Size : " + totalFiles.size());
		return totalFiles;
	}

	private static List<String> findInputFilesToSearch(String input_path) {
		List<String> fileList = new ArrayList<String>();
		File inputFile = new File(input_path);
		if (inputFile.exists()) {
			List<File> directories = findAllSubdirs(inputFile);
			for (File directory : directories) {
				File[] tempList = new File(directory.getAbsolutePath()).listFiles();
				if (tempList != null && tempList.length > -1) {
					for (File file : tempList) {
						if (file.isFile() && file.getName().endsWith(".java"))
							fileList.add(file.getName());
					}
				}
			}
		}
		return fileList;
	}

	private static List<File> findAllSubdirs(File file) {
		List<File> subdirs = Arrays.asList(file.listFiles(File::isDirectory));
		subdirs = new ArrayList<File>(subdirs);
		List<File> deepSubdirs = new ArrayList<File>();
		for (File subdir : subdirs) {
			deepSubdirs.addAll(findAllSubdirs(subdir));
		}
		subdirs.addAll(deepSubdirs);
		return subdirs;
	}

	private static List<File> totalFilesInDirectories(List<File> input) {
		List<File> totalFileList = new ArrayList<>();
		for (File file : input) {
			totalFileList.addAll(Arrays.asList(file.listFiles()));
		}
		return totalFileList;
	}

	public Map<String, List<String>> getSingleHierarchy(String key) {
		if (key != null) {
			key = (!key.endsWith(".java")) ? key.concat(".java") : key;
			return scanCodeBase.singleClasshierarchy(key);
		}
		return new HashMap<String, List<String>>();
	}

}
