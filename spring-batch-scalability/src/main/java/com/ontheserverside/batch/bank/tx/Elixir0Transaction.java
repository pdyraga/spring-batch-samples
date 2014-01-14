package com.ontheserverside.batch.bank.tx;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents Elixir0 domestic transaction loaded from file.
 *
 * Elixir0 is a country-domestic format used by Polish banking systems.
 * It may contain standard credit transfer transactions as well as social
 * security and tax payments.
 */
public final class Elixir0Transaction {

    private int paymentCode;
    private Date paymentDate;
    private BigDecimal amount;
    private String orderingPartySortCode;
    private String orderingPartyAccountNumber;
    private String beneficiaryAccountNumber;
    private String orderingPartyName;
    private String orderingPartyAddress;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String beneficiarySortCode;

    // fields below are used only for social security and tax payments
    private String payersNip;
    private String identifierType;
    private String payersIdentification;
    private String paymentType;
    private Date paymentPeriod;
    private String periodFormNumber;
    private String additionallCaseID;

    private String transactionCode;
    private String clientBankInformation;

    public int getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(int paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderingPartySortCode() {
        return orderingPartySortCode;
    }

    public void setOrderingPartySortCode(String orderingPartySortCode) {
        this.orderingPartySortCode = orderingPartySortCode;
    }

    public String getOrderingPartyAccountNumber() {
        return orderingPartyAccountNumber;
    }

    public void setOrderingPartyAccountNumber(String orderingPartyAccountNumber) {
        this.orderingPartyAccountNumber = orderingPartyAccountNumber;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public String getOrderingPartyName() {
        return orderingPartyName;
    }

    public void setOrderingPartyName(String orderingPartyName) {
        this.orderingPartyName = orderingPartyName;
    }

    public String getOrderingPartyAddress() {
        return orderingPartyAddress;
    }

    public void setOrderingPartyAddress(String orderingPartyAddress) {
        this.orderingPartyAddress = orderingPartyAddress;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getBeneficiarySortCode() {
        return beneficiarySortCode;
    }

    public void setBeneficiarySortCode(String beneficiarySortCode) {
        this.beneficiarySortCode = beneficiarySortCode;
    }

    public String getPayersNip() {
        return payersNip;
    }

    public void setPayersNip(String payersNip) {
        this.payersNip = payersNip;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getPayersIdentification() {
        return payersIdentification;
    }

    public void setPayersIdentification(String payersIdentification) {
        this.payersIdentification = payersIdentification;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(Date paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public String getPeriodFormNumber() {
        return periodFormNumber;
    }

    public void setPeriodFormNumber(String periodFormNumber) {
        this.periodFormNumber = periodFormNumber;
    }

    public String getAdditionallCaseID() {
        return additionallCaseID;
    }

    public void setAdditionallCaseID(String additionallCaseID) {
        this.additionallCaseID = additionallCaseID;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getClientBankInformation() {
        return clientBankInformation;
    }

    public void setClientBankInformation(String clientBankInformation) {
        this.clientBankInformation = clientBankInformation;
    }
}