package co.andrescol.mc.library.configuration;

import co.andrescol.mc.library.plugin.APlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.List;

public abstract class AConfigurationObject {

    public void setValues() {
        FileConfiguration configuration = APlugin.getInstance().getConfig();
        for (Field field : this.getClass().getDeclaredFields()) {
            AConfigurationKey key = field.getAnnotation(AConfigurationKey.class);
            try {
                if (field.getType().equals(String.class)) {
                    field.set(null, configuration.getString(key.value()));
                } else if (field.getType().equals(Integer.class)) {
                    field.set(null, configuration.getInt(key.value()));
                } else if (field.getType().equals(Long.class)) {
                    field.set(null, configuration.getLong(key.value()));
                } else if (field.getType().equals(List.class) && field.getGenericType().equals(String.class)) {
                    field.set(null, configuration.getStringList(key.value()));
                } else if (field.getType().equals(List.class) && field.getGenericType().equals(Integer.class)) {
                    field.set(null, configuration.getIntegerList(key.value()));
                } else if (field.getType().equals(Boolean.class)) {
                    field.set(null, configuration.getBoolean(key.value()));
                } else {
                    APlugin.getInstance().warn("Unsupported field type {} for {} config key", field.getType(), key.value());
                }
            } catch (IllegalAccessException e) {
                APlugin.getInstance().error("Error setting the value for {}", e, key.value());
            }

        }
    }
}
