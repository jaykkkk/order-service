package order_management.order_service.controller;


import lombok.RequiredArgsConstructor;
import order_management.order_service.dto.OrderRequestDTO;
import order_management.order_service.dto.OrderResponseDTO;
import order_management.order_service.models.Order;
import order_management.order_service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint methods would go here (e.g., createOrder)

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {

        orderService.createOrder(orderRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());

    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        // Implementation for fetching order by ID would go here
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        // Implementation for updating order by ID would go here
        return ResponseEntity.ok(orderService.updateOrder(id, orderRequestDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        // Implementation for deleting order by ID would go here
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }


}
