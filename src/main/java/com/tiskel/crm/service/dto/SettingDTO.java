package com.tiskel.crm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.crm.domain.Setting} entity.
 */
public class SettingDTO implements Serializable {

    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    @NotNull
    private String defaultValue;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingDTO)) {
            return false;
        }

        SettingDTO settingDTO = (SettingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
