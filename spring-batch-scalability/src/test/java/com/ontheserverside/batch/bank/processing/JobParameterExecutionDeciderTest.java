package com.ontheserverside.batch.bank.processing;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class JobParameterExecutionDeciderTest {

    private static final String JOB_PARAMETER_KEY = "someFancyKey";

    private JobParameterExecutionDecider decider;

    @Before
    public void setUp() {
        this.decider = new JobParameterExecutionDecider();
        decider.setJobParameterKey(JOB_PARAMETER_KEY);
    }

    @Test
    public void shouldReturnFlowStatusFromJobParameter() {
        JobParameters jobParameters = new JobParametersBuilder()
          .addString(JOB_PARAMETER_KEY, "PUFF")
          .toJobParameters();

        JobExecution jobExecution = new JobExecution(1L, jobParameters);
        StepExecution stepExecution = new StepExecution("myStep", jobExecution);

        final FlowExecutionStatus flowExecutionStatus = decider.decide(jobExecution, stepExecution);

        assertThat(flowExecutionStatus, is(not(nullValue())));
        assertThat(flowExecutionStatus, is(new FlowExecutionStatus("PUFF")));
    }
}
