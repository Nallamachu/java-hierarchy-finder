package com.hierarchy.finder.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
