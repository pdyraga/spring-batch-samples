package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.TransactionStatus;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cleans up stale date in case of re-entering sanction screening step during the job recovery.
 * Because of multithreaded nature of this step and unknown number of matches that are going to
 * be analysed for one transaction, this strategy fits better for this task than processing
 * status indicator column (can't really say if transaction has been completely screened or not).
 */
public final class SanctionScreeningRecoveryCleaner extends StepExecutionListenerSupport {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void beforeStep(StepExecution stepExecution) {

        sessionFactory.getCurrentSession()
                .createQuery("delete from SanctionMatch where transaction.id in (" +
                        "select tx.id from Elixir0Transaction as tx where tx.status = :status)")
                .setParameter("status", TransactionStatus.LOADED)
                .executeUpdate();
    }
}
