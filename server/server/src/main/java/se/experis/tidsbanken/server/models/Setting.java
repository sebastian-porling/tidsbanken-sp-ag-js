package se.experis.tidsbanken.server.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public class Setting {
    @Id
    private String id;
    @NotNull
    private String key;
    @NotNull
    private Object value;

    public Setting(String key, Object value) {
        this.key = key;
        this.value = value;
    }
    public Setting() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
