package com.hierarchy.finder.scan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ScanCodeBase {
	Scanner scanner = null;
	Map<String, List<String>> dependeciesMap = new HashMap<>();

	@SuppressWarnings("unlikely-arg-type")
	public Map<String, List<String>> codeScanner(List<File> tempFiles, List<String> filesList)
			throws FileNotFoundException {
		Long startTime = System.currentTimeMillis();
		long lineNum = 0, wordCount = 0;

		if (filesList != null && filesList.size() > 0 && tempFiles != null && tempFiles.size() > 0) {
			if (!dependeciesMap.isEmpty())
				dependeciesMap.clear();
			for (File tempFile : tempFiles) {
				String name = null;
				try {
					if (tempFile.isFile() && tempFile.getName().contains(".java")) {
						scanner = new Scanner(tempFile);
						while (scanner.hasNextLine()) {
							StringBuilder line = new StringBuilder(scanner.nextLine());
							lineNum++;
							name = tempFile.getName();
							if ((line != null && !line.equals("")) && name != null && filesList.contains(name)) {
								line = new StringBuilder(
										(line != null) ? line.toString().replaceAll("[^a-zA-Z0-9]", " ") : "");
								List<String> wordAr = (line.toString().contains(" "))
										? Arrays.asList(line.toString().split(" "))
										: null;
								if (wordAr != null && wordAr.size() > 0) {
									wordCount = wordCount + wordAr.size();
									wordAr.stream().forEach(word -> {
										if (word != null && !word.equals("")
												&& Character.isUpperCase(word.codePointAt(0))) {
											StringBuilder newWord = new StringBuilder(
													word.replaceAll("[^a-zA-Z0-9]", "")).append(".java");
											if (filesList.contains(newWord.toString())) {
												List<String> value = dependeciesMap.get(newWord.toString());
												if (value != null) {
													Boolean contains = (!value.contains(tempFile.getName().toString()))
															? (value.add(tempFile.getName().toString()))
															: false;
													if (contains)
														dependeciesMap.put(newWord.toString(), value);
												} else {
													value = new ArrayList<>();
													value.add(tempFile.getName().toString());
													dependeciesMap.put(newWord.toString(), value);
												}
											}
											newWord = null;
											word = null;
										}
									});
								}
							}
							name = null;
							line = null;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Total no of lines Scanned : " + lineNum + " Total word count is: " + wordCount);
		} else {
			System.out.println("Files list is empty");
		}
		System.out
				.println("Time taken to scan files is: " + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
		return dependeciesMap;
	}

	public Map<String, List<String>> singleClasshierarchy(String key) {
		Map<String, List<String>> singleDependency = new HashMap<>();
		List<String> values = null;
		if (!dependeciesMap.isEmpty())
			values = dependeciesMap.get(key);
		if (values != null && values.size() > 0)
			singleDependency.put(key, values);
		return singleDependency;
	}

	@SuppressWarnings("resource")
	public List<String> searchForString(String input, List<File> totalFiles) {
		List<String> inputMatchIn = new ArrayList<>();
		if (!CollectionUtils.isEmpty(totalFiles) && (input != null && input.length() > 2 && !input.equalsIgnoreCase(" "))) {
			totalFiles.stream().filter(file -> (file.isFile() && file.getName().contains(".java"))).forEach(file -> {
				try {
					Scanner scanner = new Scanner(file);
					while (scanner.hasNextLine()) {
						StringBuilder line = new StringBuilder(scanner.nextLine());
						if ((line != null && line.length() > 1 && file.getName() != null
								&& isContain(line.toString(), input) && !inputMatchIn.contains(file.getName()))) {
							inputMatchIn.add(file.getName());
							break;
						}
						line = null;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
		}
		System.out.println("List of files contains the give input string is: "+inputMatchIn.size());
		return inputMatchIn;
	}
	
	private static boolean isContain(String line, String input) {
		String pattern = "\\b" + input + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		return m.find();
	}
}
