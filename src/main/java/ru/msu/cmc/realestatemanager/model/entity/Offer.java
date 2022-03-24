package ru.msu.cmc.realestatemanager.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.hibernate.annotations.Type;
import ru.msu.cmc.realestatemanager.model.util.HashMapConverter;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "offers")
@Getter
@Setter
@ToString
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

    @Column(name = "space", nullable = false)
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> space;

    @Column(name = "commodities", nullable = false)
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> commodities;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Lob
    @Column(name = "building_state", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String buildingState;

    @Column(name = "transport", nullable = false)
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object>transport;

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

}