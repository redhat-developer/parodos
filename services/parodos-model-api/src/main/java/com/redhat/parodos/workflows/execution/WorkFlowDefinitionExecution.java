package com.redhat.parodos.workflows.execution;

import com.redhat.parodos.workflows.enums.WorkFlowStatus;
import com.redhat.parodos.workflows.task.execution.WorkFlowTaskDefinitionExecution;

import java.util.Date;
import java.util.List;

public abstract class WorkFlowDefinitionExecution {
    private String workflowDefinitionId;
    private String executedFor;
    private String executedBy;
    private WorkFlowStatus status;
    private Date startDate;
    private Date endDate;
    private List<WorkFlowTaskDefinitionExecution> tasks;
}
