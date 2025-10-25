package order_management.order_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long customerId;
    private Integer quantity;
    private double price;
    private String status;
}
