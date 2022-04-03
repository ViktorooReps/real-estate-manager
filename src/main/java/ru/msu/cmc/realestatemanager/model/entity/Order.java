package ru.msu.cmc.realestatemanager.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@Transactional
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id",
            columnDefinition = "serial",
            insertable = false,
            updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ordered_by", nullable = false)
    @ToString.Exclude
    private Client orderedBy;

    @Lob
    @Column(name = "contract_type", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String contractType;

    @Column(name = "requested_estate_types", columnDefinition = "json")
    @Type(type = "json")
    private String requestedEstateTypes;

    @Column(name = "requested_estate_facades", columnDefinition = "json")
    @Type(type = "json")
    private String requestedEstateFacades;

    @Column(name = "requested_space_min", columnDefinition = "json")
    @Type(type = "json")
    private String requestedSpaceMin;

    @Column(name = "requested_commodities", columnDefinition = "json")
    @Type(type = "json")
    private String requestedCommodities;

    @Column(name = "floor_min")
    private Integer floorMin;

    @Column(name = "floor_max")
    private Integer floorMax;

    @Lob
    @Column(name = "building_state", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String buildingState;

    @Column(name = "requested_transport_max", columnDefinition = "json")
    @Type(type = "json")
    private String requestedTransportMax;

    @Column(name = "requested_locations", columnDefinition = "json")
    @Type(type = "json")
    private String requestedLocations;

    @Column(name = "price_max")
    private Integer priceMax;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getId().equals(order.getId()) && Objects.equals(getOrderedBy(), order.getOrderedBy()) && Objects.equals(getContractType(), order.getContractType()) && Objects.equals(getRequestedEstateTypes(), order.getRequestedEstateTypes()) && Objects.equals(getRequestedEstateFacades(), order.getRequestedEstateFacades()) && Objects.equals(getRequestedSpaceMin(), order.getRequestedSpaceMin()) && Objects.equals(getRequestedCommodities(), order.getRequestedCommodities()) && Objects.equals(getFloorMin(), order.getFloorMin()) && Objects.equals(getFloorMax(), order.getFloorMax()) && Objects.equals(getBuildingState(), order.getBuildingState()) && Objects.equals(getRequestedTransportMax(), order.getRequestedTransportMax()) && Objects.equals(getRequestedLocations(), order.getRequestedLocations()) && Objects.equals(getPriceMax(), order.getPriceMax());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderedBy(), getContractType(), getRequestedEstateTypes(), getRequestedEstateFacades(), getRequestedSpaceMin(), getRequestedCommodities(), getFloorMin(), getFloorMax(), getBuildingState(), getRequestedTransportMax(), getRequestedLocations(), getPriceMax());
    }
}