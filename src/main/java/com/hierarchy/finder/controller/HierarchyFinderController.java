package com.hierarchy.finder.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hierarchy.finder.manager.HierarchyManager;

@RestController
@RequestMapping(path = "/api")
public class HierarchyFinderController {

	ResponseEntity<Map<String, List<String>>> responseEntity = null;

	@Autowired
	private HierarchyManager hierarchyManager;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(path = "/hierarchy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, List<String>>> getHierarchy()
			throws FileNotFoundException, UnsupportedEncodingException {
		Map<String, List<String>> response = hierarchyManager.getHierarchy();
		if (response != null)
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return responseEntity;
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(path = "/hierarchy/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, List<String>>> getSingleHierarchy(@PathVariable String key)
			throws FileNotFoundException, UnsupportedEncodingException {
		Map<String, List<String>> response = hierarchyManager.getSingleHierarchy(key);
		if (response != null)
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return responseEntity;
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(path = "/hierarchy/search/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getStringHierarchy(@RequestParam String input)
			throws FileNotFoundException, UnsupportedEncodingException {
		ResponseEntity<List<String>> responseEntity = null;
		List<String> response = hierarchyManager.findHierarchyOfString(input);
		if (response != null)
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return responseEntity;
	}

}
