package com.redhat.parodos.workflows.definition;

import com.redhat.parodos.workflows.enums.WorkFlowType;
import com.redhat.parodos.workflows.task.definition.WorkFlowTaskDefinition;

import java.util.Date;
import java.util.List;

public abstract class WorkFlowDefinition<V>{
    private String name;
    private String description;
    private WorkFlowType type;
    private List<WorkFlowTaskDefinition<V>> tasks;
    private String version;
    private String author;
    private Date createdDate;
    private Date modifiedDate;
}
