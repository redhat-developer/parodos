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
package com.redhat.parodos.examples.continued.complex;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redhat.parodos.examples.simple.LoggingWorkFlowTask;
import com.redhat.parodos.infrastructure.option.InfrastructureOption;
import com.redhat.parodos.workflows.consts.WorkFlowConstants;
import com.redhat.parodos.workflows.workflow.ParallelFlow;
import com.redhat.parodos.workflows.workflow.SequentialFlow;
import com.redhat.parodos.workflows.workflow.WorkFlow;

/**
 * A more complex WorkFlow
 * 
 * @author Luke Shannon (Github: lshannon)
 *
 */
@Configuration
public class ComplexWorkFlowConfiguration {
	
	//Start Assessment Logic
	
	//Infrastructure Option for Onboarding
	@Bean(name = "onboardingOption")
	InfrastructureOption onboardingOption() {
		return new InfrastructureOption.Builder("onboardingOption", "onboardingWorkflow_INFRASTRUCTURE").addToDetails("An example of a WorkFlow with Status checks").build();
	}
	
	//Determines what InfrastructureOption if available based on Input
	@Bean(name = "onboardingAssessmentTask")
    OnboardingAssessment onboardingAssessment(@Qualifier("onboardingOption") InfrastructureOption awesomeToolsOption) {
        return new OnboardingAssessment(awesomeToolsOption, "OnboardingAssessment");
    }
	
    @Bean(name = "onboardingAssessment" + WorkFlowConstants.ASSESSMENT_WORKFLOW)
    WorkFlow assessmentWorkFlow(@Qualifier("onboardingAssessmentTask") OnboardingAssessment onboardingAssessmentTask) {
        return SequentialFlow
                .Builder
                .aNewSequentialFlow()
                .named("onboardingAssessment" + WorkFlowConstants.ASSESSMENT_WORKFLOW)
                .execute(onboardingAssessmentTask)
                .build();
    }
    
    //End Assessment Logic
    
    //Start Onboarding Logic
    @Bean
    LoggingWorkFlowTask certWorkFlowTask() {
    	return new LoggingWorkFlowTask("certWorkFlowTask");
    }
    
    @Bean
    LoggingWorkFlowTask adGroupWorkFlowTask() {
    	return new LoggingWorkFlowTask("adGroupWorkFlowTask");
    }
    
    @Bean
    LoggingWorkFlowTask dynatraceWorkFlowTask(@Qualifier("onboardingWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW) WorkFlow onboardingWorkFlowChecker) {
    	LoggingWorkFlowTask loggingWorkFlow = new LoggingWorkFlowTask("dynatraceWorkFlowTask");
    	loggingWorkFlow.setWorkFlowChecker(onboardingWorkFlowChecker);
    	return loggingWorkFlow;
    }
    
    
    @Bean(name = "onboardingWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
    WorkFlow superAwesomeTechnologyStackWorkflow(LoggingWorkFlowTask certWorkFlowTask, LoggingWorkFlowTask adGroupWorkFlowTask, LoggingWorkFlowTask dynatraceWorkFlowTask) {
        return ParallelFlow.Builder
        		.aNewParallelFlow()
        		.named("onboardingWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
        		.execute(certWorkFlowTask,adGroupWorkFlowTask,dynatraceWorkFlowTask)
        		.with(Executors.newFixedThreadPool(3))
        		.build();
    }
    
    // End Onboarding Logic
    
    //Start Name Space Logic
    @Bean
    LoggingWorkFlowTask nameSpaceWorkFlowTask(@Qualifier("namespaceWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW) WorkFlow gateThree) {
    	LoggingWorkFlowTask loggingWorkFlow = new LoggingWorkFlowTask("nameSpaceWorkFlowTask");
    	loggingWorkFlow.setWorkFlowChecker(gateThree);
    	return loggingWorkFlow;
    }
    
    @Bean(name = "nameSpaceWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
    WorkFlow superAwesomeTechnologyStackWorkflow(LoggingWorkFlowTask nameSpaceWorkFlowTask) {
        return SequentialFlow.Builder
        		.aNewSequentialFlow()
        		.named( "nameSpaceWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
        		.execute(nameSpaceWorkFlowTask)
        		.build();
    }
    //End Name Space Logic
    
    //Start Load Balancer Logic
    
    @Bean(name = "loadBalancerFlowTask")
    LoggingWorkFlowTask loadBalancerFlowTask() {
    	return new LoggingWorkFlowTask("loadBalancerFlowTask");
    }
    
    @Bean(name = "failOverWorkFlowTask")
    LoggingWorkFlowTask failOverWorkFlowTask() {
    	return new LoggingWorkFlowTask("failOverWorkFlowTask");
    }
    
    @Bean(name = "networkingWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
    WorkFlow superAwesomeTechnologyStackWorkflow(@Qualifier("loadBalancerFlowTask") LoggingWorkFlowTask networkingFlowTask,@Qualifier("failOverWorkFlowTask") LoggingWorkFlowTask failOverWorkFlowTask ) {
        return SequentialFlow.Builder
        		.aNewSequentialFlow()
        		.named( "networkingWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
        		.execute(networkingFlowTask)
        		.then(failOverWorkFlowTask)
        		.build();
    }
    
    //End Load Balancer Logic
    
    
    //Start onboardingWorkFlowCheck Logic
    
    @Bean(name = "onboardingWorkFlowCheck")
    MockApprovalWorkFlowCheckerTask gateTwo() {
    	return new MockApprovalWorkFlowCheckerTask("nameSpaceWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW, "onboardingWorkFlowCheck");
    }
    
    @Bean(name = "onboardingWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
    WorkFlow onboardingWorkFlowCheckWorkFlow(@Qualifier("onboardingWorkFlowCheck") MockApprovalWorkFlowCheckerTask gateTwo) {
        return SequentialFlow.Builder
        		.aNewSequentialFlow()
        		.named( "onboardingWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
        		.execute(gateTwo)
        		.build();
    }
    
    //End onboardingWorkFlowCheck Logic
    
    //Start namespaceWorkFlowCheck Logic
    
    @Bean(name = "namespaceWorkFlowCheck")
    MockApprovalWorkFlowCheckerTask gateThree() {
    	return new MockApprovalWorkFlowCheckerTask("networkingWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW, "namespaceWorkFlowCheck");
    }
    
    @Bean(name = "namespaceWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
    WorkFlow namespaceWorkFlowCheckWorkFlow(@Qualifier("namespaceWorkFlowCheck") MockApprovalWorkFlowCheckerTask gateThree) {
        return SequentialFlow.Builder
        		.aNewSequentialFlow()
        		.named( "namespaceWorkFlowCheckWorkFlow" + WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)
        		.execute(gateThree)
        		.build();
    }
    
    //End namespaceWorkFlowCheck Logic
    
}
