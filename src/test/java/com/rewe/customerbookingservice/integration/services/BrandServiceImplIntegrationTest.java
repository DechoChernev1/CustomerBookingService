package com.rewe.customerbookingservice.integration.services;

import com.rewe.customerbookingservice.dtos.BrandDTO;
import com.rewe.customerbookingservice.services.BrandService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrandServiceImplIntegrationTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("integration-tests-db")
            .withUsername("test_user")
            .withPassword("test_user_pass");

    @Autowired
    private BrandService brandService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "brand");
    }

    @Test
    void saveBrand_shouldSaveAndReturnBrandDTO() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand A");

        BrandDTO savedBrand = brandService.saveBrand(brandDTO);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getName()).isEqualTo("Brand A");
    }

    @Test
    void findBrandById_shouldReturnBrandDTO_whenBrandExists() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand B");
        BrandDTO savedBrand = brandService.saveBrand(brandDTO);

        Optional<BrandDTO> result = brandService.findBrandById(savedBrand.getId());

        assertTrue(result.isPresent());
        assertEquals(savedBrand.getId(), result.get().getId());
    }

    @Test
    void findAllBrands_shouldReturnListOfBrandDTOs() {
        BrandDTO brandDTO1 = new BrandDTO();
        brandDTO1.setName("Brand X");
        BrandDTO brandDTO2 = new BrandDTO();
        brandDTO2.setName("Brand Y");

        brandService.saveBrand(brandDTO1);
        brandService.saveBrand(brandDTO2);

        List<BrandDTO> brands = brandService.findAllBrands();

        assertThat(brands).hasSize(2);
    }

    @Test
    void deleteBrand_shouldRemoveBrand() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Brand Z");
        BrandDTO savedBrand = brandService.saveBrand(brandDTO);

        brandService.deleteBrand(savedBrand.getId());

        Optional<BrandDTO> result = brandService.findBrandById(savedBrand.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void updateBrand_shouldUpdateAndReturnBrandDTO_whenBrandExists() {
        BrandDTO initial = new BrandDTO();
        initial.setName("Initial Brand");
        BrandDTO savedBrand = brandService.saveBrand(initial);

        BrandDTO updateDetails = new BrandDTO();
        updateDetails.setName("Updated Brand");

        BrandDTO updatedBrand = brandService.updateBrand(savedBrand.getId(), updateDetails);

        assertThat(updatedBrand).isNotNull();
        assertThat(updatedBrand.getName()).isEqualTo("Updated Brand");

        Optional<BrandDTO> fetchedBrand = brandService.findBrandById(savedBrand.getId());
        assertThat(fetchedBrand).isPresent();
        assertThat(fetchedBrand.get().getName()).isEqualTo("Updated Brand");
    }
}
