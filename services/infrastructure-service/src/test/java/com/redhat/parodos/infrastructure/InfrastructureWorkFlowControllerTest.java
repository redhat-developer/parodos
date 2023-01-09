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
package com.redhat.parodos.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.parodos.workflow.execution.transaction.WorkFlowTransactionEntity;
import com.redhat.parodos.workflows.consts.WorkFlowConstants;
import com.redhat.parodos.workflows.task.WorkFlowTaskParameter;
import com.redhat.parodos.workflows.task.WorkFlowTaskParameterType;
import com.redhat.parodos.workflows.work.DefaultWorkReport;
import com.redhat.parodos.workflows.work.WorkContext;
import com.redhat.parodos.workflows.work.WorkReport;
import com.redhat.parodos.workflows.work.WorkStatus;

/**
 * Test class for InfrastructureWorkFlowController
 *
 * @author Richard Wang (Github: RichardW98)
 */
@WebMvcTest(InfrastructureWorkFlowController.class)
@AutoConfigureMockMvc(addFilters = false)
class InfrastructureWorkFlowControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private WorkFlowTransactionEntity entity;

    @MockBean
    private InfrastructureWorkFlowService infrastructureWorkFlowService;

    @Test
    void executeWorkFlow_success_shouldReturn_Uri() throws Exception {
        List<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.randomUUID());
        WorkContext workContext = new WorkContext();
        workContext.put(WorkFlowConstants.WORKFLOW_EXECUTION_ENTITY_REFERENCES, uuids.get(0));
        WorkReport workReport = new DefaultWorkReport(WorkStatus.COMPLETED, workContext);

        when(infrastructureWorkFlowService.execute(any())).thenReturn(workReport);
        mockMvc.perform(
                        post("/api/v1/workflows/infrastructures/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(Map.of("workFlowId", "test", "workFlowParameters", Map.of()))))
                .andDo(print())
                .andExpect(status().isOk());
        verify(infrastructureWorkFlowService, times(1)).execute(any());
    }

    @Test
    void getInfraStructureTaskWorkFlows_success_shouldReturn_Workflows() throws Exception {
        when(infrastructureWorkFlowService.getInfrastructureTaskWorkFlows(WorkFlowConstants.INFRASTRUCTURE_WORKFLOW)).thenReturn(List.of("test_workflow"));
        mockMvc.perform(
                        get("/api/v1/workflows/infrastructures/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$", Matchers.contains("test_workflow")));
        verify(infrastructureWorkFlowService, times(1)).getInfrastructureTaskWorkFlows(any());
    }

    @Test
    void getInfraStructureTaskWorkFlowDescription_success_shouldReturn_WorkflowParameters() throws Exception {
        String id = "test_id";
        when(infrastructureWorkFlowService.getWorkFlowParametersForWorkFlow(id)).thenReturn(List.of(
                WorkFlowTaskParameter.builder()
                        .key("param_1")
                        .type(WorkFlowTaskParameterType.TEXT)
                        .optional(false)
                        .build(),
                WorkFlowTaskParameter.builder()
                        .key("param_2")
                        .type(WorkFlowTaskParameterType.PASSWORD)
                        .optional(true)
                        .build()));
        mockMvc.perform(get("/api/v1/workflows/infrastructures/" + id + "/parameters"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].key", Matchers.containsInAnyOrder("param_1", "param_2")));
        verify(infrastructureWorkFlowService, times(1)).getWorkFlowParametersForWorkFlow(any());
    }
}
