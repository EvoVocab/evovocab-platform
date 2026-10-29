package com.evovocab.shared.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class IdUUID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
}
