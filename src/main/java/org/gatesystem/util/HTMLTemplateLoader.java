package org.gatesystem.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTMLTemplateLoader {
    public static String load(String path, Map<String, String> values) {
        try (InputStream is = HTMLTemplateLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Template not found for path: " + path);
            }

            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            for (var entry : values.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return template;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load template", e);
        }
    }
}
