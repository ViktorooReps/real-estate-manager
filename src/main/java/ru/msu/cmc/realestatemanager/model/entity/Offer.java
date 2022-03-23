package ru.msu.cmc.realestatemanager.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "offers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "offered_by", nullable = false)
    private Client offeredBy;

    @Lob
    @Column(name = "contract_type", nullable = false)
    private String contractType;

    @Lob
    @Column(name = "estate_type", nullable = false)
    private String estateType;

    @Lob
    @Column(name = "estate_facade", nullable = false)
    private String estateFacade;

    @Column(name = "space", nullable = false)
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode space;

    @Column(name = "commodities", nullable = false)
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode commodities;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Lob
    @Column(name = "building_state", nullable = false)
    private String buildingState;

    @Column(name = "transport", nullable = false)
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode transport;

    @Lob
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "starting_price", nullable = false)
    private Integer startingPrice;

    @Lob
    @Column(name = "address", nullable = false)
    private String address;
}