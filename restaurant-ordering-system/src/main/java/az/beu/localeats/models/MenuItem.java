package az.beu.localeats.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Yeməyin adı
    private String description; // Yeməyin tərkibi/izahı
    private Double price; // Qiyməti
    private String imageUrl; // Şəkli

    private boolean isAvailable = true; // Mövcudluq (Stokda var/yox)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
