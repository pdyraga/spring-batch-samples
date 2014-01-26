package com.ontheserverside.batch.bank.tx;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents Elixir0 domestic transaction loaded from file.
 *
 * Elixir0 is a country-domestic format used by Polish banking systems.
 * It may contain standard credit transfer transactions as well as social
 * security and tax payments.
 */
public final class Elixir0Transaction {

    /**
     * Names of fields declared as a 'text type' fields by the elixir0 format
     */
    private static final String[] TEXT_FIELDS = {
            "orderingPartyAccountNumber",
            "beneficiaryAccountNumber",
            "orderingPartyNameAndAddress",
            "beneficiaryNameAndAddress",
            "paymentDetails",
            "transactionCode",
            "clientBankInformation"
    };

    private static final ToStringStyle TO_STRING_STYLE = new TxToStringStyle();

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

    private String paymentDetails;

    // fields below are used only for social security and tax payments
    // their values should be extracted from 'paymentDetails' section
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

    public String getPaymentDetails() { return paymentDetails; }

    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
                .append("paymentCode", paymentCode)
                .append("paymentDate", paymentDate)
                .append("amount", amount)
                .append("orderingPartySortCode", orderingPartySortCode)
                .append("0") // hardcoded as '0' in the format definition
                .append("orderingPartyAccountNumber", orderingPartyAccountNumber)
                .append("beneficiaryAccountNumber", beneficiaryAccountNumber)
                .append("orderingPartyNameAndAddress", String.format("%s|%s",
                        orderingPartyName != null ? orderingPartyName : "",
                        orderingPartyAddress != null ? orderingPartyAddress : ""))
                .append("beneficiaryNameAndAddress", String.format("%s|%s",
                        beneficiaryName != null ? beneficiaryName : "",
                        beneficiaryAddress != null ? beneficiaryAddress : ""))
                .append("0") // hardcoded as '0' in the format definition
                .append("beneficiarySortCode", beneficiarySortCode)
                .append("paymentDetails", paymentDetails)
                .append("") // hardcoded as empty in the format definition
                .append("") // hardcoded as empty in the format definition
                .append("transactionCode", transactionCode)
                .append("clientBankInformation", clientBankInformation)
                .toString();
    }

    private static class TxToStringStyle extends ToStringStyle {

        TxToStringStyle() {
            super();
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
            this.setUseFieldNames(false);
            this.setContentStart("");
            this.setContentEnd("");
            this.setNullText("");
        }

        @Override
        protected void appendFieldStart(StringBuffer buffer, String fieldName) {
            super.appendFieldStart(buffer, fieldName);

            if (ArrayUtils.contains(TEXT_FIELDS, fieldName)) {
                buffer.append("\"");
            }
        }

        @Override
        protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
            if (ArrayUtils.contains(TEXT_FIELDS, fieldName)) {
                buffer.append("\"");
            }

            super.appendFieldEnd(buffer, fieldName);
        }

        @Override
        protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value instanceof Date) {
                value = new SimpleDateFormat("yyyyMMdd").format(value);
            }

            buffer.append(value);
        }
    }
}