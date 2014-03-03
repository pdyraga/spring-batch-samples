package com.ontheserverside.batch.bank.screening;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Immutable
@Table(name = "SDN_ENTITY")
public final class SDNEntity {

    @Id
    @Column(name = "UID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany
    @JoinColumn(name = "ENTITY_UID")
    private List<SDNAddress> addresses;

    @ElementCollection
    @CollectionTable(
            name = "SDN_ALTERNATE_NAME",
            joinColumns=@JoinColumn(name = "ENTITY_UID")
    )
    @Column(name = "NAME")
    private List<String> alternateNames;

    // hibernate requirement
    @Deprecated
    protected SDNEntity() {
    }

    public SDNEntity(String name, List<SDNAddress> addresses, List<String> alternateNames) {
        this.name = name;
        this.addresses = addresses;
        this.alternateNames = alternateNames;
    }

    public String getName() {
        return this.name;
    }

    public List<SDNAddress> getAddresses() {
        return this.addresses;
    }

    public List<String> getAlternateNames() {
        return this.alternateNames;
    }

    @Entity
    @Immutable
    @Table(name = "SDN_ADDRESS")
    public static class SDNAddress {

        @Id
        @Column(name = "ADDRESS_UID")
        private long id;

        @Column(name = "ADDRESS")
        private String address;

        @Column(name = "CITY")
        private String city;

        @Column(name = "COUNTRY")
        private String country;

        // hibernate requirement
        @Deprecated
        protected SDNAddress() {
        }

        public SDNAddress(String address, String city, String country) {
            this.address = address;
            this.city = city;
            this.country = country;
        }

        public String getFull() {
          return new StringBuilder()
                  .append(StringUtils.defaultString(this.address)).append(" ")
                  .append(StringUtils.defaultString(this.city)).append(" ")
                  .append(StringUtils.defaultString(this.country))
                  .toString();
        }
    }
}
