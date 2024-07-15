package pl.pomoku.inventorymanagementapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "file_type")
    private String fileType;

    @Column(nullable = true, name = "image_url")
    @Formula("concat('http://localhost:8080/api/v1/products/', product_id, '/images/', id)")
    private String imageUrl;

    @Lob
    @Column(name = "image_data", length = 1024)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
