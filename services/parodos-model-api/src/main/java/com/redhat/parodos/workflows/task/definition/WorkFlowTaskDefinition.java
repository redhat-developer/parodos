package com.redhat.parodos.workflows.task.definition;

import com.redhat.parodos.workflows.enums.WorkFlowTaskOutput;
import com.redhat.parodos.workflows.task.WorkFlowTaskParameter;

import java.util.List;
import java.util.Map;

public abstract class WorkFlowTaskDefinition<V> {
    private String workFlowDefinitionId;
    private String name;
    private String description;
    private Map<WorkFlowTaskParameter, V> inputData;
    private List<WorkFlowTaskOutput> outputData;
    private WorkFlowTaskDefinition<V> previousTask;
    private WorkFlowTaskDefinition<V> nextTask;
}