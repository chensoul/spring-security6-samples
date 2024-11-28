package com.chensoul.security;

import com.chensoul.security.entity.Product;
import com.chensoul.security.repository.ProductRepository;
import com.chensoul.security.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class})
class MainTests {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static List<Product> products = new ArrayList<>();

    @BeforeAll
    public static void init() {
        products.add(new Product(1, "beer", "nikolai"));
        products.add(new Product(2, "candy", "nikolai"));
        products.add(new Product(3, "chocolate", "julien"));
    }

    @Test
    void testProductServiceWithNoUser() {
        assertThrows(AuthenticationException.class, () -> productService.sellProducts(products));
    }

    @Test
    @WithMockUser(username = "julien")
    void testProductServiceWithUser() {
        var result = productService.sellProducts(products);
        var expected = List.of(new Product(3, "chocolate", "julien"));

        assertEquals(expected, result);
    }

    @Test
        void testProductRepositoryWithNoUser() {
        var result = productRepository.findProductByNameContains("c");

        assertTrue(result.isEmpty());
    }

    @Test
    @WithMockUser(username = "julien")
    void testProductRepositoryWithUser() {
        var result = productRepository.findProductByNameContains("c");

        result.forEach(p -> {
            assertEquals("julien", p.getOwner());
            assertTrue(p.getName().contains("c"));
        });
    }
}
