package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(columnDefinition = "varchar(20)", unique = true, nullable = false)
    String name;

    public Role(String name){
        this.name = name;
    }
}
