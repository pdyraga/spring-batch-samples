package com.ontheserverside.batch.bank.screening;

import com.ontheserverside.batch.bank.tx.Elixir0Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import static java.util.Arrays.asList;

public final class SanctionScreeningProcessor implements ItemProcessor<SanctionScreeningContext, SanctionMatch> {

    private FuzzyMatcher fuzzyMatcher;

    private final AbstractMatcher matcher = new CompositeMatcher(asList(
            new BeneficiaryNameMatcher(),
            new OrderingPartyNameMatcher(),
            new BeneficiaryAddressMatcher(),
            new OrderingPartyAddressMatcher(),
            new BeneficiaryAlternateNameMatcher(),
            new OrderingPartyAlternateNameMatcher()
    ));

    @Override
    public SanctionMatch process(SanctionScreeningContext context) {
        final Elixir0Transaction transaction =  context.getTransaction();
        final SDNEntity sdnEntity = context.getSdnEntity();

        if (matcher.matches(transaction, sdnEntity)) {
          return new SanctionMatch(transaction, sdnEntity);
        }

        return null;
    }

    private final class BeneficiaryNameMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            return fuzzyMatch(tx.getBeneficiaryName(), sdn.getName());
        }
    }

    private final class OrderingPartyNameMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            return fuzzyMatch(tx.getOrderingPartyName(), sdn.getName());
        }
    }

    private final class BeneficiaryAddressMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            final List<String> fullAddresses = new LinkedList<>();
            for (SDNEntity.SDNAddress address : sdn.getAddresses()) {
                fullAddresses.add(address.getFull());
            }
            return fuzzyMatch(tx.getBeneficiaryAddress(), fullAddresses);
        }
    }

    private final class OrderingPartyAddressMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            final List<String> fullAddresses = new LinkedList<>();
            for (SDNEntity.SDNAddress address : sdn.getAddresses()) {
                fullAddresses.add(address.getFull());
            }
            return fuzzyMatch(tx.getOrderingPartyAddress(), fullAddresses);
        }
    }

    private final class BeneficiaryAlternateNameMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            return fuzzyMatch(tx.getBeneficiaryName(), sdn.getAlternateNames());
        }
    }

    private final class OrderingPartyAlternateNameMatcher extends AbstractMatcher {
        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            return fuzzyMatch(tx.getOrderingPartyName(), sdn.getAlternateNames());
        }
    }

    private final class CompositeMatcher extends AbstractMatcher {

        private final List<AbstractMatcher> matchers;

        public CompositeMatcher(List<AbstractMatcher> matchers) {
          this.matchers = matchers;
        }

        @Override
        public boolean matches(Elixir0Transaction tx, SDNEntity sdn) {
            for (AbstractMatcher matcher: matchers) {
                if (matcher.matches(tx, sdn)) {
                    return true;
                }
            }

            return false;
        }
    }

    private abstract class AbstractMatcher {

        public abstract boolean matches(Elixir0Transaction tx, SDNEntity sdn);

        protected boolean fuzzyMatch(String str, String searchStr) {
            return fuzzyMatcher.sequencesMatching(str, searchStr);
        }

        protected boolean fuzzyMatch(String str, Iterable<String> searchStrs) {
            for (String searchStr : searchStrs) {
                if (fuzzyMatch(str, searchStr)) {
                    return true;
                }
            }

            return false;
        }
    }
}
