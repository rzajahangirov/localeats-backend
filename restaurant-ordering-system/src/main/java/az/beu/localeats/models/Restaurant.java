package az.beu.localeats.models;

import az.beu.localeats.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Restoranın adı

    @Column(length = 500)
    private String description; // Təsviri

    private String cuisineType; // Mətbəx növü

    private String address; // Ünvan

    private String openingHours; // İş saatları

    private String coverImage; // Qapaq şəkli URL-i

    @Column(nullable = false)
    private String phoneNumber; // Restoranın əlaqə nömrəsi

    @Column(nullable = false)
    private String contactEmail; // Restoranın rəsmi email ünvanı

    @Enumerated(EnumType.STRING)
    private RestaurantStatus status = RestaurantStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // Restoranın sahibi

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories; // Menyu kateqoriyaları
}