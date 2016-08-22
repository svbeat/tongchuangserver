package com.tongchuang.visiondemo.relationship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.relationship.entity.Relationship;

@RestController
@CrossOrigin
public class RelationshipController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RelationshipRepository 	relationshipRepository;
	
	
	@Autowired
	public RelationshipController(RelationshipRepository relationshipRepository) {
		this.relationshipRepository = relationshipRepository;
	}
	

	@RequestMapping(value = "/relationships", method = RequestMethod.POST)
	public ResponseEntity<Relationship> createRelationship(@RequestParam("apiKey") String apiKey, 
															@RequestBody Relationship relationship) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Relationship>(HttpStatus.UNAUTHORIZED);
		}
		relationshipRepository.save(relationship);
		relationship = relationshipRepository.findOne(relationship.getRelationshipId());
		return new ResponseEntity<Relationship>(relationship, HttpStatus.CREATED);
	}
		

	@RequestMapping(value = "/relationships/{relationshipId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteRelationship(@PathVariable("relationshipId") Integer relationshipId, @RequestParam("apiKey") String apiKey) {
		
		logger.info("deleteRelationship: relationshipId="+relationshipId);

		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		Relationship relationship = relationshipRepository.findOne(relationshipId);
		if (relationship == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
		}
		
		relationship.setDeleted(EntityDeleted.Y);
		relationshipRepository.save(relationship);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
