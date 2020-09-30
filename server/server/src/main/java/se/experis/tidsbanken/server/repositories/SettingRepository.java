package se.experis.tidsbanken.server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import se.experis.tidsbanken.server.models.Setting;

import java.util.Optional;


public interface SettingRepository extends MongoRepository<Setting, String> {
    Boolean existsByKey(String key);
    Optional<Setting> findByKey(String key);
}
