package ru.msu.cmc.realestatemanager.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "client_name")
    private String clientName;

    @Lob
    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "phone_number")
    private String phoneNumber;
}