package com.ontheserverside.batch.bank.job;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;
import com.ontheserverside.batch.bank.tx.TransactionStatus;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.AbstractPollableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("test")
public class Elixir0LoadStepTest {

    @Configuration
    @ComponentScan("com.ontheserverside.batch.bank")
    @ImportResource({ "classpath:/META-INF/spring/hibernateContext.xml",
                      "classpath:/META-INF/spring/batchContext.xml" })
    static class ApplicationContext {
        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
          return new JobLauncherTestUtils();
        }

        @Bean
        public TaskExecutor executor() {
            return new ThreadPoolTaskExecutor();
        }

        @Bean(name = { "screening.requests.partitioning",
                       "screening.replies.partitioning"})
        public MessageChannel channels() {
            return new AbstractPollableChannel() {
                @Override
                protected Message<?> doReceive(long timeout) {
                    throw new UnsupportedOperationException("I'm just mocked test channel");
                }

                @Override
                protected boolean doSend(Message<?> message, long timeout) {
                    throw new UnsupportedOperationException("I'm just mocked test channel");
                }
            };
        }
    }

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void shouldLoadElixir0TxFromFile() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputFile", this.getClass().getResource("/elixir0/5Tx.elixir0").getPath())
                .toJobParameters();

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep("elixir0LoadStep", jobParameters);

        final StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getExitStatus(), is(ExitStatus.COMPLETED));
        assertThat(stepExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(stepExecution.getReadCount(), is(5)); // 5 is a number of TX in the imported file
        assertThat(stepExecution.getWriteCount(), is(5));

        final List<Elixir0Transaction> importedTransactions =
                sessionFactory.openSession()
                        .createCriteria(Elixir0Transaction.class)
                        .addOrder(Order.asc("paymentDate"))
                        .list();
        assertThat(importedTransactions.size(), is(5));

        // we'll test mapping of the representative transaction, the rest can be proved by induction ;-)
        final Elixir0Transaction transaction = importedTransactions.get(0);
        assertThat(transaction.getStatus(), is(TransactionStatus.LOADED));
        assertThat(transaction.getPaymentCode(), is(110));
        assertThat(transaction.getPaymentDate().getTime(), is(new SimpleDateFormat("yyyyMMdd").parse("20140212").getTime()));
        assertThat(transaction.getAmount().compareTo(BigDecimal.valueOf(828749700184127L)), is(0));
        assertThat(transaction.getOrderingPartySortCode(), is("68578953"));
        assertThat(transaction.getOrderingPartyAccountNumber(), is("87685789534046696210183491"));
        assertThat(transaction.getBeneficiaryAccountNumber(), is("81878754069967016032059101"));
        assertThat(transaction.getOrderingPartyName(), is("SOUTHBOUND LTD."));
        assertThat(transaction.getOrderingPartyAddress(), is("8876 Harvest Leaf Harbour, Fighting Rock Corner, Maryland, 21069-9972, US"));
        assertThat(transaction.getBeneficiaryName(), is("Vincenza Henkle"));
        assertThat(transaction.getBeneficiaryAddress(), is("4489 Sunny Hickory Terrace, Enderby, Iowa, 50157-9431, US"));
        assertThat(transaction.getBeneficiarySortCode(), is("87875406"));
        assertThat(transaction.getPaymentDetails(), is("passkey deleaves rimmed sarsens sponsor"));
        assertThat(transaction.getTransactionCode(), is("51"));
        assertThat(transaction.getClientBankInformation(), is(""));
    }
}
