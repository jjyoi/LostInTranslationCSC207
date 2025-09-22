package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the services of: <br/>
 * - converting language codes to their names <br/>
 * - converting language names to their codes
 */
public class LanguageCodeConverter {

    private final Map<String, String> languageCodeToLanguage = new HashMap<>();
    private final Map<String, String> languageToLanguageCode = new HashMap<>();

    /**
     * Default constructor that loads the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(
                    getClass().getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                iterator.next(); // skip header line
            }
            while (iterator.hasNext()) {
                String line = iterator.next();
                // 忽略空行和以#开头的注释
                if (line == null) continue;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // 兼容逗号或制表符分隔
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length < 2) {
                    parts = line.split("\\t+");
                }
                if (parts.length < 2) continue; // 格式不对，跳过

                String language = parts[0].trim();
                String code = parts[1].trim().toLowerCase();


                if (!code.isEmpty() && !language.isEmpty()) {
                    languageCodeToLanguage.put(code, language);
                    languageToLanguageCode.put(language.toLowerCase(), code);
                }
            }

        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException("Failed to load resource: " + filename, ex);
        }
    }


    /**
     * Return the name of the language for the given language code.
     * @param code the 2-letter language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        if (code == null) {
            return null;
        }

        return languageCodeToLanguage.get(code);
    }

    /**
     * Return the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        if (language == null) {
            return null;
        }
        return languageToLanguageCode.get(language.toLowerCase());
    }

    /**
     * Return how many languages are included in this language code converter.
     * @return how many languages are included in this language code converter.
     */
    public int getNumLanguages() {
        return languageCodeToLanguage.size();
    }
}
