package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;

import java.io.Serializable;

public final class SanctionScreeningContext implements Serializable {

    private final Elixir0Transaction transaction;
    private final SDNEntity sdnEntity;

    public SanctionScreeningContext(Elixir0Transaction transaction, SDNEntity sdnEntity) {
        this.transaction = transaction;
        this.sdnEntity = sdnEntity;
    }

    public Elixir0Transaction getTransaction() {
        return this.transaction;
    }

    public SDNEntity getSdnEntity() {
        return this.sdnEntity;
    }
}
