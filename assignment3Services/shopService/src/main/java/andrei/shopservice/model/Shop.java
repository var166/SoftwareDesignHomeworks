package andrei.shopservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Builder
@Data
@Table(name = "shops")
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;
    private String phone;

    @Column(unique = true)
    private String email;

    private String description;
    private Long adminId;

    @ElementCollection
    @CollectionTable(name = "shop_product_stock", joinColumns = @JoinColumn(name = "shop_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "stock")
    @Builder.Default
    private Map<Long, Integer> productStock = new HashMap<>();
}
