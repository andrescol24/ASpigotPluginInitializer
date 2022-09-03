package co.andrescol.mc.library.configuration;

import co.andrescol.mc.library.plugin.APlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AConfigurationObject {

    public void setValues() {
        FileConfiguration configuration = APlugin.getInstance().getConfig();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                AConfigurationKey key = field.getAnnotation(AConfigurationKey.class);
                if (key == null) continue;
                field.setAccessible(true);
                Class<?> type = field.getType();
                // String
                if (String.class.equals(type)) {
                    field.set(this, configuration.getString(key.value()));
                }
                // Integer
                else if (Integer.class.equals(type) || int.class.equals(type)) {
                    field.set(this, configuration.getInt(key.value()));
                }
                // Long
                else if (Long.class.equals(type) || long.class.equals(type)) {
                    field.set(this, configuration.getLong(key.value()));
                }
                // Boolean
                else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                    field.setBoolean(this, configuration.getBoolean(key.value()));
                }
                // Lists
                else if (field.getType().equals(List.class)) {
                    Class<?> genericType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    // String list
                    if (String.class.equals(genericType)) {
                        field.set(this, configuration.getStringList(key.value()));
                    }
                    // Integer list
                    else if (Integer.class.equals(genericType)) {
                        field.set(this, configuration.getIntegerList(key.value()));
                    }
                } else {
                    APlugin.getInstance().warn("Unsupported field type {}<{}> for {} config variable", field.getType(), field.getGenericType(), field.getName());
                }
            } catch (Exception e) {
                APlugin.getInstance().error("Error setting the value for {}", e, field.getName());
            }

        }
    }
}
