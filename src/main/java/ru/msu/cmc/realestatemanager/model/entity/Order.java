package ru.msu.cmc.realestatemanager.model.entity;

import com.bazarnazar.pgjson.JsonMapType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.msu.cmc.realestatemanager.model.util.HashMapConverter;

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
    @Type(type = "org.hibernate.type.TextType")
    private String contractType;

    @Column(name = "requested_estate_types")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedEstateTypes;

    @Column(name = "requested_estate_facades")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedEstateFacades;

    @Column(name = "requested_space_min")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedSpaceMin;

    @Column(name = "requested_commodities")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedCommodities;

    @Column(name = "floor_min")
    private Integer floorMin;

    @Column(name = "floor_max")
    private Integer floorMax;

    @Lob
    @Column(name = "building_state", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String buildingState;

    @Column(name = "requested_transport_max")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedTransportMax;

    @Column(name = "requested_locations")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> requestedLocations;

    @Column(name = "price_max")
    private Integer priceMax;
}