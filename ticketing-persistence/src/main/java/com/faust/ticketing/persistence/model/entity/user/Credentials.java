package com.faust.ticketing.persistence.model.entity.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Credentials {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Size(min = 186, max = 186)
    private char[] password;

    @NotBlank
    @Size(max = 100)
    private String pepperAlias;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
