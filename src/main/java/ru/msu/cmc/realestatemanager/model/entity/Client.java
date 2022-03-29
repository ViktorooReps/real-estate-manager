package ru.msu.cmc.realestatemanager.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString
@Transactional
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id",
            columnDefinition = "serial",
            insertable = false,
            updatable = false)
    private Integer id;

    @Column(name = "client_name")
    @Type(type = "org.hibernate.type.TextType")
    private String clientName;

    @Column(name = "email")
    @Type(type = "org.hibernate.type.TextType")
    private String email;

    @Column(name = "phone_number")
    @Type(type = "org.hibernate.type.TextType")
    private String phoneNumber;
}