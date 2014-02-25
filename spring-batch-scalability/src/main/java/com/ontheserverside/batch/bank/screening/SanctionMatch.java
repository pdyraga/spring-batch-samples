package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;

import javax.persistence.*;

@Entity
@Table(name = "SANCTION_MATCH")
public class SanctionMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "TRANSACTION_ID")
    private Elixir0Transaction transaction;

    @OneToOne
    @JoinColumn(name = "SDN_ENTITY_ID")
    private SDNEntity sdnEntity;

    public SanctionMatch(Elixir0Transaction transaction, SDNEntity sdnEntity) {
        this.transaction = transaction;
        this.sdnEntity = sdnEntity;
    }

    public Elixir0Transaction getTransaction() {
        return transaction;
    }

    public SDNEntity getSdnEntity() {
        return sdnEntity;
    }
}
