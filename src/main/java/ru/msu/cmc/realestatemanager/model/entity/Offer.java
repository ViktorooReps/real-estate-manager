package ru.msu.cmc.realestatemanager.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "offers")
@Getter
@Setter
@ToString
@Transactional
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id", columnDefinition = "serial",
            insertable = false,
            updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "offered_by", nullable = false)
    private Client offeredBy;

    @Lob
    @Column(name = "contract_type", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String contractType;

    @Lob
    @Column(name = "estate_type", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String estateType;

    @Lob
    @Column(name = "estate_facade", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String estateFacade;

    @Column(name = "space", nullable = false, columnDefinition = "json")
    @Type(type = "json")
    private String space;

    @Column(name = "commodities", nullable = false, columnDefinition = "json")
    @Type(type = "json")
    private String commodities;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Lob
    @Column(name = "building_state", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String buildingState;

    @Column(name = "transport", nullable = false, columnDefinition = "json")
    @Type(type = "json")
    private String transport;

    @Lob
    @Column(name = "location", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String location;

    @Column(name = "starting_price", nullable = false)
    private Integer startingPrice;

    @Lob
    @Column(name = "address", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return getId().equals(offer.getId()) && Objects.equals(getOfferedBy(), offer.getOfferedBy()) && getContractType().equals(offer.getContractType()) && Objects.equals(getEstateType(), offer.getEstateType()) && Objects.equals(getEstateFacade(), offer.getEstateFacade()) && Objects.equals(getSpace(), offer.getSpace()) && Objects.equals(getCommodities(), offer.getCommodities()) && Objects.equals(getFloor(), offer.getFloor()) && Objects.equals(getBuildingState(), offer.getBuildingState()) && Objects.equals(getTransport(), offer.getTransport()) && Objects.equals(getLocation(), offer.getLocation()) && Objects.equals(getStartingPrice(), offer.getStartingPrice()) && Objects.equals(getAddress(), offer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOfferedBy(), getContractType(), getEstateType(), getEstateFacade(), getSpace(), getCommodities(), getFloor(), getBuildingState(), getTransport(), getLocation(), getStartingPrice(), getAddress());
    }
}