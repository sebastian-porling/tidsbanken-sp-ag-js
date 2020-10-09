package se.experis.tidsbanken.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import se.experis.tidsbanken.server.models.Setting;

import java.util.Optional;


/**
 * Handles mongodb methods for Settings
 */
public interface SettingRepository extends MongoRepository<Setting, String> {

    /**
     * Checks if setting exists by setting name
     * @param key setting name
     * @return True if it exists
     */
    Boolean existsByKey(String key);

    /**
     * Finds setting by setting name
     * @param key setting name
     * @return Optional with setting or empty
     */
    Optional<Setting> findByKey(String key);
}
