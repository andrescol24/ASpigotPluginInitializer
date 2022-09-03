package co.andrescol.mc.library.utils;

public interface AUtils {

    /**
     * Replace all {} values
     *
     * @param message      message
     * @param replacements replacements
     * @return String values replaced
     */
    static String replaceValues(String message, Object... replacements) {
        for (Object replace : replacements) {
            String valueToReplace = String.valueOf(replace);
            message = message.replaceFirst("\\{}", valueToReplace);
        }
        return message;
    }
}
