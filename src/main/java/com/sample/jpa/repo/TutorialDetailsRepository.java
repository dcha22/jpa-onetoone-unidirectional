package com.sample.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.jpa.model.TutorialDetails;

import jakarta.transaction.Transactional;

// Test comment
public interface TutorialDetailsRepository extends JpaRepository<TutorialDetails, Long> {
	@Transactional
	void deleteById(long id);
	
	@Transactional
	void deleteByTutorialId (long tutorialId);
}
