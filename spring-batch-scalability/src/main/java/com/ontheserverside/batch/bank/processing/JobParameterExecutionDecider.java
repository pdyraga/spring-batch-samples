package com.ontheserverside.batch.bank.processing;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Required;

public final class JobParameterExecutionDecider implements JobExecutionDecider {

    private String jobParameterKey;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        return new FlowExecutionStatus(jobExecution.getJobParameters().getString(jobParameterKey));
    }

    @Required
    public void setJobParameterKey(String jobParameterKey) {
        this.jobParameterKey = jobParameterKey;
    }
}
