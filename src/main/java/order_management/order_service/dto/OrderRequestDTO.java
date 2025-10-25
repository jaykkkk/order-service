package order_management.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderRequestDTO {
    private Long productId;
    private Long customerId;
    private Integer quantity;
    private Double price;
    private String orderStatus;
}
