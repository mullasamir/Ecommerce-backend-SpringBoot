package com.ecommerce.request;

import com.ecommerce.model.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateProductRequest {
    private String title;
    private String description;
    private Integer price;

    private Integer discountedPrice;
    private Integer discountPresent;

    private Integer quantity;
    private String brand;
    private String color;
    private Set<Size> sizes = new HashSet<>();

    private String imageUrl;

    private String topLevelCategory;
    private String secondLevelCategory;
    private String thirdLevelCategory;


}
