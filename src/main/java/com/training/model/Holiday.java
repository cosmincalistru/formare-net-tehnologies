package com.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @SequenceGenerator(name= "hol_seq", sequenceName = "holidays_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hol_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "free_type")
    private String freeType;

    @Column(name = "startDate")
    private Date startDate;

    public Holiday() {
    }
}
