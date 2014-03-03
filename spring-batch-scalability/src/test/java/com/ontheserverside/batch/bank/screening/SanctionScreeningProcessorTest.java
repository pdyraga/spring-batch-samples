package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SanctionScreeningProcessorTest {

    private static final String BENEFICIARY_NAME = "Brandon Beer";
    private static final String ORDERING_PARTY_NAME = "Marcelle Rhee";
    private static final String BENEFICIARY_ADDRESS_FULL = "Sienkiewicza 10 Kielce Poland";
    private static final String ORDERING_PARTY_ADDRESS_FULL = "Meerschweinchen Platz 10 Berlin Germany";

    private static final String SDN_ENTITY_NAME = "Daniele Hagen";
    private static final String SDN_ENTITY_ALTERNATE_NAME = "Melanie Blundell";
    private static final String SDN_ENTITY_ADDRESS = "Sunnyside 13C";
    private static final String SDN_ENTITY_CITY = "New York";
    private static final String SDN_ENTITY_COUNTRY = "USA";
    private static final String SDN_ENTITY_ADDRESS_FULL = "Sunnyside 13C New York USA";

    private FuzzyMatcher mockFuzzyMatcher;

    private SanctionScreeningProcessor processor;

    private SanctionScreeningContext context;

    @Before
    public void setUp() {
        this.mockFuzzyMatcher = createMock(FuzzyMatcher.class);
        this.processor = new SanctionScreeningProcessor();
        ReflectionTestUtils.setField(processor, "fuzzyMatcher", mockFuzzyMatcher);

        Elixir0Transaction transaction = new Elixir0Transaction();
        transaction.setBeneficiaryName(BENEFICIARY_NAME);
        transaction.setBeneficiaryAddress(BENEFICIARY_ADDRESS_FULL);
        transaction.setOrderingPartyName(ORDERING_PARTY_NAME);
        transaction.setOrderingPartyAddress(ORDERING_PARTY_ADDRESS_FULL);

        SDNEntity.SDNAddress sdnAddress = new SDNEntity.SDNAddress(SDN_ENTITY_ADDRESS, SDN_ENTITY_CITY, SDN_ENTITY_COUNTRY);
        List<SDNEntity.SDNAddress> sdnAddresses = new LinkedList<>();
        sdnAddresses.add(sdnAddress);

        List<String> sdnAlternateNames = new LinkedList<>();
        sdnAlternateNames.add(SDN_ENTITY_ALTERNATE_NAME);

        SDNEntity sdnEntity = new SDNEntity(SDN_ENTITY_NAME, sdnAddresses, sdnAlternateNames);

        this.context = new SanctionScreeningContext(transaction, sdnEntity);
    }

    @Test
    public void shouldMatchSanctionForBeneficaryName() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldMatchSanctionForOrderingPartyName() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldMatchSanctionForBeneficiaryAddress() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldMatchSanctionForOrderingPartyAddress() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldMatchSanctionForBeneficiaryAlternateName() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_ALTERNATE_NAME)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldMatchSanctionForOrderingPartyAlternateName() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_ALTERNATE_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_ALTERNATE_NAME)).andReturn(true);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertSanctionMatched(match);
    }

    @Test
    public void shouldNotWarnWhenSDNIsNotMatching() {
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_ADDRESS_FULL, SDN_ENTITY_ADDRESS_FULL)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(BENEFICIARY_NAME, SDN_ENTITY_ALTERNATE_NAME)).andReturn(false);
        expect(mockFuzzyMatcher.sequencesMatching(ORDERING_PARTY_NAME, SDN_ENTITY_ALTERNATE_NAME)).andReturn(false);
        replay(mockFuzzyMatcher);

        final SanctionMatch match = processor.process(context);

        verify(mockFuzzyMatcher);
        assertThat(match, is(nullValue()));
    }

    private void assertSanctionMatched(final SanctionMatch match) {
        assertThat(match, is(not(nullValue())));
        assertThat(match.getSdnEntity(), is(context.getSdnEntity()));
        assertThat(match.getTransaction(), is(context.getTransaction()));
    }
}
