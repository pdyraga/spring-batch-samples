package com.ontheserverside.batch.bank.controller;

import com.ontheserverside.batch.bank.tx.Elixir0Generator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("/")
public final class RootController {

    private static final Log logger = LogFactory.getLog(RootController.class);

    @Autowired
    private Elixir0Generator elixir0Generator;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job elixir0ImportJob;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String goHome() {
      return "home";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public @ResponseBody Callable<String> generateElixir0(
            @RequestParam("destinationFilePath") final String destinationFilePath,
            @RequestParam("numberOfTransactions") final int numberOfTransactions) throws IOException {

        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info(String.format("Generating Elixir0 message with %d transactions into %s",
                        numberOfTransactions, destinationFilePath));
                elixir0Generator.generate(new File(destinationFilePath), numberOfTransactions);
                return "Elixir0 message generated";
            }
        };
    }

    @RequestMapping(value = "/startJob", method = RequestMethod.POST)
    public @ResponseBody String startJob(
            @RequestParam("inputFilePath") final String inputFilePath) throws JobExecutionException {

        logger.info(String.format("Starting import job for Elixir0 message %s", inputFilePath));

        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputFile", inputFilePath)
                .toJobParameters();
        final JobExecution jobExecution = jobLauncher.run(elixir0ImportJob, jobParameters);
        return "Job started with ID=" + jobExecution.getJobId();
    }
}
