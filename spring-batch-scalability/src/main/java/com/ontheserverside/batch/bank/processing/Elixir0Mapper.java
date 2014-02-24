package com.ontheserverside.batch.bank.processing;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;
import com.ontheserverside.batch.bank.tx.TransactionStatus;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public final class Elixir0Mapper implements FieldSetMapper<Elixir0Transaction> {

    /**
     * Elixir0 message column names definition as used by {@link org.springframework.batch.item.file.transform.LineTokenizer}.
     */
    public static final String COLUMN_NAMES = "paymentCode,paymentDate,amount,orderingPartySortCode,unused1,orderingPartyAccountNumber,"
                    + "beneficiaryAccountNumber,orderingPartyNameAndAddress,beneficiaryNameAndAddress,unused2,beneficiarySortCode,"
                    + "paymentDetails,unused3,unused4,transactionCode,clientBankInformation";


    @Override
    public Elixir0Transaction mapFieldSet(final FieldSet fieldSet) throws BindException {

        final Elixir0Transaction tx = new Elixir0Transaction();
        tx.setStatus(TransactionStatus.LOADED);

        tx.setPaymentCode(fieldSet.readInt("paymentCode"));
        tx.setPaymentDate(fieldSet.readDate("paymentDate", "yyyyMMdd"));
        tx.setAmount(fieldSet.readBigDecimal("amount"));
        tx.setOrderingPartySortCode(fieldSet.readString("orderingPartySortCode"));
        tx.setOrderingPartyAccountNumber(fieldSet.readString("orderingPartyAccountNumber"));
        tx.setBeneficiaryAccountNumber(fieldSet.readString("beneficiaryAccountNumber"));

        final String[] orderingPartyNameAndAddress = fieldSet.readString("orderingPartyNameAndAddress").split("\\|", 2);
        tx.setOrderingPartyName(orderingPartyNameAndAddress[0]);
        tx.setOrderingPartyAddress(orderingPartyNameAndAddress[1]);

        final String[] beneficiaryNameAndAddress = fieldSet.readString("beneficiaryNameAndAddress").split("\\|", 2);
        tx.setBeneficiaryName(beneficiaryNameAndAddress[0]);
        tx.setBeneficiaryAddress(beneficiaryNameAndAddress[1]);

        tx.setBeneficiarySortCode(fieldSet.readString("beneficiarySortCode"));
        tx.setPaymentDetails(fieldSet.readString("paymentDetails"));
        tx.setTransactionCode(fieldSet.readString("transactionCode"));
        tx.setClientBankInformation(fieldSet.readString("clientBankInformation"));

        return tx;
    }
}
