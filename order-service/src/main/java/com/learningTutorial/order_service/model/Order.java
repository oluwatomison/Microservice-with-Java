package com.learningTutorial.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String OrderNumber;
    @OneToMany(cascade = CascadeType.ALL) // CascadeType.ALL means that all the operations will be performed on the related entity
    private List<OrderLineItems> orderLineItemsList; // Work on the naming convention

}
