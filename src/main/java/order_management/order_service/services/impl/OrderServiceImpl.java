package order_management.order_service.services.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import order_management.order_service.dto.CustomerDTO;
import order_management.order_service.dto.OrderRequestDTO;
import order_management.order_service.dto.OrderResponseDTO;
import order_management.order_service.dto.ProductDTO;
import order_management.order_service.enums.OrderStatusEnum;
import order_management.order_service.feign.CustomerClient;
import order_management.order_service.feign.ProductClient;
import order_management.order_service.models.Order;
import order_management.order_service.repo.OrderRepository;
import order_management.order_service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final CustomerClient customerClient;

    public OrderServiceImpl(OrderRepository orderRepository, ProductClient productClient, CustomerClient customerClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.customerClient = customerClient;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "productFallBack")
    private ProductDTO getProductWithFallBack(Long id){
        return productClient.getProductById(id);
    };
    public ProductDTO productFallBack(Long id, Throwable throwable){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setName("Unknown Product");
        productDTO.setDescription("N/A");
        productDTO.setPrice(0.0);

        return productDTO;
    }
    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallBack")
    private CustomerDTO getCustomerWithFallBack(Long id){
        return customerClient.getCustomerById(id);
    };
    public CustomerDTO customerFallBack(Long id, Throwable throwable){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(id);
        customerDTO.setName("Unknown Customer");
        customerDTO.setEmail("email.com");
        customerDTO.setPhone("N/A");
        return customerDTO;
    }


    @Override
    public void createOrder(OrderRequestDTO orderRequestDTO) {

        ProductDTO product = productClient.getProductById(orderRequestDTO.getProductId());
        CustomerDTO customer = customerClient.getCustomerById(orderRequestDTO.getCustomerId());


        //validateOrderRequest(orderRequestDTO);
        Order order=   Order.builder().
                productId(product.getId()).
                customerId(customer.getId()).
                price(product.getPrice()*orderRequestDTO.getQuantity()).
                quantity(orderRequestDTO.getQuantity()).
                status(OrderStatusEnum.CREATED.name())
                .build();
        orderRepository.save(order);


    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        // Map Order to OrderResponseDTO
        // Implement the mapping logic here and return the OrderResponseDTO

        return OrderResponseDTO.builder().id(order.getId())

                .quantity(order.getQuantity())
                .price(order.getPrice())
                .orderStatus(order.getStatus())
                .build();

    }

    @CachePut(value = "orders", key = "#id")
    @Override
    public String updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPrice(orderRequestDTO.getPrice());
        order.setQuantity(orderRequestDTO.getQuantity());
        orderRepository.save(order);
        return "Order updated successfully";
    }

    @CacheEvict(value = "orders", key = "#id")
    @Override
    public String deleteOrderById(Long id) {
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            return "Order deleted successfully";
        }
        return "";
    }
}
