package com.redhat.parodos.workflows.task.execution;

import com.redhat.parodos.workflows.enums.WorkFlowStatus;

import java.util.Date;
import java.util.Map;

public class WorkFlowTaskDefinitionExecution {
    private String workflowDefinitionExecutionId;
    private String workflowTaskDefinitionId;
    private WorkFlowStatus status;
    private Map inputData;
    private Date startDate;
    private Date endDate;
}
