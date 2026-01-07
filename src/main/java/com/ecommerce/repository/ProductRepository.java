package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("""
select p from Product p
where (:category is null or p.category.name = :category)
and ((:minPrice is null and :maxPrice is null) 
     or p.discountedPrice between :minPrice and :maxPrice)
and (:minDiscount is null or p.discountPresent >= :minDiscount)
order by 
case when :sort = 'price_low' then p.discountedPrice end asc,
case when :sort = 'price_high' then p.discountedPrice end desc
"""
    )

    public List<Product> filterProducts(@Param("category")String category,
                                        @Param("minPrice")Integer minPrice,
                                        @Param("maxPrice")Integer maxPrice,
                                        @Param("minDiscount")Integer minDiscount,
                                        @Param("sort")String sort);
}
