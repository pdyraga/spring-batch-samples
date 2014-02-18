/*
 * Copyright (C) the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ontheserverside.batch.bank.tx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Component
public final class SimpleElixir0Generator implements Elixir0Generator {

    @javax.annotation.Resource
    @Value("classpath:names.txt")
    private Resource paymentPartyNamesResource;

    @javax.annotation.Resource
    @Value("classpath:addresses.txt")
    private Resource paymentPartyAddressesResource;

    @javax.annotation.Resource
    @Value("classpath:dictionary.txt")
    private Resource dictionaryResource;

    private final List<String> paymentPartyNames = new LinkedList<>();
    private final List<String> paymentPartyAddresses = new LinkedList<>();
    private final List<String> words = new LinkedList<>();

    @PostConstruct
    private void loadExternalDataResources() throws IOException {
        paymentPartyNames.addAll(loadLinesFromFile(paymentPartyNamesResource));
        paymentPartyAddresses.addAll(loadLinesFromFile(paymentPartyAddressesResource));
        words.addAll(loadLinesFromFile(dictionaryResource));
    }

    // note: usage of this method is reasonable only for relatively small files!
    private List<String> loadLinesFromFile(final Resource resource) throws IOException {
        return Files.readAllLines(resource.getFile().toPath(),
                StandardCharsets.UTF_8);
    }

    @Override
    public void generate(final File outputFile, final int numberOfTransactions) throws IOException {
        final TxGenerator txGenerator = new TxGenerator();
        final BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath(), StandardCharsets.UTF_8);
        try {
            for (int i=0; i<numberOfTransactions; i++) {
                writer.write(txGenerator.generate().toString());
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private class TxGenerator {

        private static final int PAYMENT_DETAILS_MAX_NUMBER_OF_WORDS = 6;

        private final Random random = new Random();

        private Elixir0Transaction generate() {
            final Elixir0Transaction transaction = new Elixir0Transaction();

            transaction.setPaymentCode(Elixir0Transaction.PAYMENT_CODE_LOCAL); // hardcoded - always local CT
            transaction.setPaymentDate(date());
            transaction.setAmount(amount());
            final String orderingPartySortCode = sortCode();
            transaction.setOrderingPartySortCode(orderingPartySortCode);
            transaction.setOrderingPartyAccountNumber(accountNumber(orderingPartySortCode, internalAccountNumber()));
            final String beneficiarySortCode = sortCode();
            transaction.setBeneficiaryAccountNumber(accountNumber(beneficiarySortCode, internalAccountNumber()));
            transaction.setOrderingPartyName(partyName());
            transaction.setOrderingPartyAddress(partyAddress());
            transaction.setBeneficiaryName(partyName());
            transaction.setBeneficiaryAddress(partyAddress());
            transaction.setBeneficiarySortCode(beneficiarySortCode);
            transaction.setPaymentDetails(paymentDetails());
            transaction.setTransactionCode(Elixir0Transaction.TRANSACTION_CODE_CT); // hardcoded - always standard CT

            return transaction;
        }

        /**
         * Generates random date between 'now' and one month ahead.
         */
        private Date date() {
            final Calendar calendar = Calendar.getInstance();
            final long now = calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1);
            final long oneMonthLater = calendar.getTimeInMillis();

            return new Date(randomBetween(now, oneMonthLater));
        }

        /**
         * Random number, 15 digits at maximum
         */
        private BigDecimal amount() {
            return new BigDecimal(randomBetween(1, 999999999999999L));
        }

        /**
         * 8-digit random number
         */
        private String sortCode() {
            return Long.toString(randomBetween(10000000, 99999999));
        }

        /**
         * Valid PL account number containing bank sort code and internal account number
         * with checksum computed.
         */
        private String accountNumber(final String sortCode, final String internalAccNumber) {
            final String checksum = accountChecksum(new BigInteger(
                    sortCode + internalAccNumber + "2521")); // PL = 2521
            return checksum + sortCode + internalAccNumber;
        }

        /**
         * 16-digit random number
         */
        private String internalAccountNumber() {
            return Long.toString(randomBetween(1000000000000000L, 9999999999999999L));
        }

        /**
         * 2-digit account checksum code (IBAN formula - ISO 7064 Mod 97,10)
         */
        private String accountChecksum(BigInteger accNr) {
            final BigInteger _98 = BigInteger.valueOf(98);
            final BigInteger _100 = BigInteger.valueOf(100);
            final BigInteger _97 = BigInteger.valueOf(97);

            return ((_98.subtract(accNr.multiply(_100).mod(_97))).mod(_97)).toString();
        }

        /**
         * Random payment party name from the predefined list
         */
        private String partyName() {
            return paymentPartyNames.get((int) randomBetween(0, paymentPartyNames.size()-1));
        }

        /**
         * Random payment party address from the predefined list
         */
        private String partyAddress() {
            return paymentPartyAddresses.get((int) randomBetween(0, paymentPartyAddresses.size()-1));
        }

        /**
         * String with random number of words (1 to {@code PAYMENT_DETAILS_MAX_NUMBER_OF_WORDS} inclusive)
         * from the predefined list
         */
        private String paymentDetails() {
           final long numberOfWords = randomBetween(1, PAYMENT_DETAILS_MAX_NUMBER_OF_WORDS);
           final StringBuffer paymentDetails = new StringBuffer();
           for (int i = 0; i < numberOfWords; i++) {
               paymentDetails.append(words.get((int) randomBetween(0, words.size())));
               paymentDetails.append(" ");
           }
           return paymentDetails.toString().trim();
        }

        private long randomBetween(long min, long max) {
            return min + ((long) (random.nextDouble() * (max - min)));
        }
    }
}
