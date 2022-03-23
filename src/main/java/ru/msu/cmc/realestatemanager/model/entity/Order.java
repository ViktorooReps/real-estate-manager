package ru.msu.cmc.realestatemanager.model.entity;

import com.bazarnazar.pgjson.JsonMapType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonMapType.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ordered_by", nullable = false)
    @ToString.Exclude
    private Client orderedBy;

    @Lob
    @Column(name = "contract_type", nullable = false)
    private String contractType;

    @Column(name = "requested_estate_types")
    @Type(type = "json")
    private Map<String, Boolean> requestedEstateTypes;

    @Column(name = "requested_estate_facades")
    @Type(type = "json")
    private Map<String, Boolean> requestedEstateFacades;

    @Column(name = "requested_space_min")
    @Type(type = "json")
    private Map<String, Integer> requestedSpaceMin;

    @Column(name = "requested_commodities")
    @Type(type = "json")
    private Map<String, Boolean> requestedCommodities;

    @Column(name = "floor_min")
    private Integer floorMin;

    @Column(name = "floor_max")
    private Integer floorMax;

    @Lob
    @Column(name = "building_state", nullable = false)
    private String buildingState;

    @Column(name = "requested_transport_max")
    @Type(type = "json")
    private Map<String, Integer> requestedTransportMax;

    @Column(name = "requested_locations")
    @Type(type = "json")
    private Map<String, Boolean> requestedLocations;

    @Column(name = "price_max")
    private Integer priceMax;
}