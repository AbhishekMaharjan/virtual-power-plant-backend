package com.virtual_power_plant.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "battery")
@BatchSize(size = 500)
@SQLDelete(sql = "UPDATE battery SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Battery extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "postcode")
    private Long postcode;

    @Column(name = "capacity")
    private Double capacity;

    @Builder.Default
    @Column(name = "is_deleted")
    @JsonIgnore
    private Boolean isDeleted = false;
}

