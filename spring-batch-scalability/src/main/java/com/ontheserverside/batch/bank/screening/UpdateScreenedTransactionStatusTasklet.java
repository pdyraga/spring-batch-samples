package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.TransactionStatus;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public final class UpdateScreenedTransactionStatusTasklet implements Tasklet {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        sessionFactory.getCurrentSession()
                .createQuery("update Elixir0Transaction as tx set tx.status = :status where tx in (" +
                        "select sm.transaction from SanctionMatch as sm)")
                .setParameter("status", TransactionStatus.SUSPENDED)
                .executeUpdate();

        sessionFactory.getCurrentSession()
                .createQuery("update Elixir0Transaction as tx set tx.status = :status where tx not in (" +
                        "select sm.transaction from SanctionMatch as sm)")
                .setParameter("status", TransactionStatus.ACCEPTED)
                .executeUpdate();

        return RepeatStatus.FINISHED;
    }
}
