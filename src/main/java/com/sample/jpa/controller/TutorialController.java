package com.sample.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.jpa.exception.ResourceNotFoundException;
import com.sample.jpa.model.Tutorial;
import com.sample.jpa.repo.TutorialDetailsRepository;
import com.sample.jpa.repo.TutorialRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class TutorialController {

	@Autowired
	TutorialRepository tutorialRepository;

	@Autowired
	TutorialDetailsRepository detailsRepository;
	
	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required=false) String title) {
		List<Tutorial> tutorials = new ArrayList<>();

		if(title == null) 
			tutorialRepository.findAll().forEach(tutorials::add);
		else 
			tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

		if(tutorials.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tutorials, HttpStatus.OK);
	}

	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		Tutorial createdTutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), true));
		return new ResponseEntity<>(createdTutorial, HttpStatus.CREATED);
	}

	//Update a tutorial
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Tutorial updatedTutorial = tutorialRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("not found Tutorial with id "+ id));

		updatedTutorial.setTitle(tutorial.getTitle());
		updatedTutorial.setDescription(tutorial.getDescription());
		updatedTutorial.setPublished(tutorial.isPublished());

		return new ResponseEntity<>(tutorialRepository.save(updatedTutorial), HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		//if (detailsRepository.existsById(id)) {
		//	detailsRepository.deleteById(id);
		//}

		tutorialRepository.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		tutorialRepository.deleteAll();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

		if (tutorials.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(tutorials, HttpStatus.OK);
	}
}
