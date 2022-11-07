/*
 * Copyright (c) 2022 Red Hat Developer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.parodos.assessment;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.redhat.parodos.infrastructure.option.InfrastructureOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * Client facing entry point for submitting a request to assess a workload and determine potential InfrastructureOption(s)
 * 
 * @author Luke Shannon (Github: lshannon)
 *
 */
@CrossOrigin(origins = "*", maxAge = 1800)
@RestController
@RequestMapping("/v1/assessments")
@Slf4j
public class AssessmentController {
	
	private final AssessmentService assessmentService;
	
	public AssessmentController(AssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	/**
	 * Any request for Assessment will be posted to this endpoint
	 * 
	 * @param assessmentRequest contains all the information needed to perform the assessment
	 * @return InfrastructureOptions containing details on the current Infrastructure and any upgrade, migration or new infrastructure options
	 */
	@PostMapping("/")
	public ResponseEntity<InfrastructureOptions> assessApplication(@RequestBody AssessmentRequestDto assessmentRequest) {
		log.debug("Running a Assessment using AssessmentRequest: {}", assessmentRequest.toString());
		return ResponseEntity.ok(assessmentService.getInfrastructureOptions(assessmentRequest));
	}
	
	/**
	 * Gets All the AssessmentWorkFlows
	 * 
	 * @return Registered AssessmentWorkFlows
	 */
	@GetMapping("/")
	public ResponseEntity<Collection<String>> getAssessmentWorkFlows() {
		return ResponseEntity.ok(assessmentService.getAssessmentWorkFlowIds());
	}
}