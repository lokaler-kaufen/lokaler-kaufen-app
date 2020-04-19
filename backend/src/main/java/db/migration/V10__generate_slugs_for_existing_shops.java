package db.migration;

import de.qaware.mercury.business.shop.SlugService;
import de.qaware.mercury.business.shop.impl.SlugServiceImpl;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Flyway java migration which adds a slug to existing shops.
 * <p>
 * See https://flywaydb.org/documentation/migrations#java-based-migrations
 */
@Slf4j
@SuppressWarnings("java:S101") // Shut up SonarQube - Flyway expects the class in this format.
public class V10__generate_slugs_for_existing_shops extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        log.debug("Generating slugs for existing shops");
        SlugService slugService = new SlugServiceImpl();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

        // Find all existing shops
        List<ShopRow> shops = jdbcTemplate.query("SELECT id, name, slug FROM shop", new ShopRowMapper());
        Set<String> createdSlugs = new HashSet<>();
        for (ShopRow shop : shops) {
            // Generate a slug for every shop and store it in the database
            String slug = slugService.generateShopSlug(shop.getName(), s -> !createdSlugs.contains(s));
            jdbcTemplate.update("UPDATE shop SET slug = ? WHERE id = ?", slug, shop.id);
            // Mark the slug as used
            createdSlugs.add(slug);
        }

        log.debug("Generated slugs for {} shops", shops.size());
    }

    @Override
    public Integer getChecksum() {
        return 278740; // Change this if you change something in here!
    }

    static class ShopRowMapper implements RowMapper<ShopRow> {
        @Override
        public ShopRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ShopRow((UUID) rs.getObject("id"), rs.getString("name"), rs.getString("slug"));
        }
    }

    @Value
    static class ShopRow {
        UUID id;
        String name;
        String slug;
    }
}
