package com.evovocab.shared.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@MappedSuperclass
@Getter
@Setter
public abstract class IdUUID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @JdbcTypeCode(SqlTypes.BINARY) // Use BINARY(16) for UUID storage
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
}
