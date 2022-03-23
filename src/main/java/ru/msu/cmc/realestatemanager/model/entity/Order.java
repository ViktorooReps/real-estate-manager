package ru.msu.cmc.realestatemanager.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ordered_by", nullable = false)
    private Client orderedBy;

    @Lob
    @Column(name = "contract_type", nullable = false)
    private String contractType;

    @Column(name = "requested_estate_types")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedEstateTypes;

    @Column(name = "requested_estate_facades")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedEstateFacades;

    @Column(name = "requested_space_min")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedSpaceMin;

    @Column(name = "requested_commodities")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedCommodities;

    @Column(name = "floor_min")
    private Integer floorMin;

    @Column(name = "floor_max")
    private Integer floorMax;

    @Lob
    @Column(name = "building_state", nullable = false)
    private String buildingState;

    @Column(name = "requested_transport_max")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedTransportMax;

    @Column(name = "requested_locations")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType")
    private JsonNode requestedLocations;

    @Column(name = "price_max")
    private Integer priceMax;
}