package com.jamiltonquintero.strategyexample.reports.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    Long id;
    String name;
}
