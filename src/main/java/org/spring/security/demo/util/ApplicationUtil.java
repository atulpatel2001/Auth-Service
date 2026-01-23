package org.spring.security.demo.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The ApplicationUtil utility class provides a collection of static methods for common operations,
 * making it easier to reuse code and improve code readability and maintainability.
 *
 * @author Jasmin.Kachhadiya
 * @since 1.0
 */
@UtilityClass
public class ApplicationUtil {

    /**
     * Checks if the given object is null or empty.
     *
     * <p>This method uses Spring's {@link ObjectUtils#isEmpty(Object)} method to determine if the
     * given object is null or empty.
     *
     * @param obj The object to check for emptiness.
     * @return {@code true} if the given object is null or empty, {@code false} otherwise.
     */
    public Boolean isEmpty(Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    /**
     * Compares two objects for equality, returning {@code true} if they are equal and {@code false}
     * otherwise. This method uses Spring's {@link ObjectUtils#nullSafeEquals(Object, Object)}
     * method.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public Boolean equals(Object obj1, Object obj2) {
        return ObjectUtils.nullSafeEquals(obj1, obj2);
    }

    /**
     * Compares two strings for equality, ignoring case, and returns {@code true} if they are equal
     * and {@code false} otherwise. This method checks if the first string is not empty before
     * performing the comparison.
     *
     * @param str1 The first string to compare.
     * @param str2 The second string to compare.
     * @return {@code true} if the strings are equal (ignoring case), {@code false} otherwise.
     */
    public Boolean equalsIgnoreCase(String str1, String str2) {
        return !isEmpty(str1) && str1.equalsIgnoreCase(str2);
    }

    /**
     * Converts a string into a list of objects using a provided delimiter and a mapping function.
     *
     * <p>This function splits the input string using the provided delimiter and applies a mapping
     * function
     * to each non-empty trimmed value. The resulting list of objects is returned.
     *
     * @param strngToList The string to convert into a list.
     * @param delimiter   The delimiter used to split the string.
     * @param mapper      The mapping function to apply to each non-empty trimmed value.
     * @param <T>         The type of the objects in the resulting list.
     * @return A list of objects resulting from applying the mapping function to each non-empty
     * trimmed value. If the input string is null or empty, an empty list is returned.
     */
    public static <T> List<T> convertStringToList(String strngToList,
                                                  String delimiter,
                                                  Function<String, T> mapper) {
        List<T> listString = new ArrayList<T>();

        if (!isEmpty(strngToList)) {
            listString = Stream.of(strngToList.split(delimiter))
                    .map(val -> mapper.apply(val.trim()))
                    .collect(Collectors.toList());
        }
        return listString;
    }

    /**
     * Checks if the given number is not null and not equal to zero.
     *
     * @param number The number to check.
     * @return {@code true} if the number is not null and not equal to zero, {@code false} otherwise.
     */
    public <T extends Number> boolean isNotNullOrZero(T number) {
        return number != null && (number.intValue() != 0 || number.doubleValue() != 0.0);
    }

    public static List<String> separateString(String text, String delimeter) {
        StringTokenizer tokenizer = new StringTokenizer(text, delimeter);
        List<String> returnList = new LinkedList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            returnList.add(token);
        }
        return returnList;
    }

    public static List<String> separateRequestParamString(String text) {
        return separateString(text, "&");
    }

    public static String leftPad(String input, int size, char padChar) {
        if (input == null) return null;
        if (input.length() >= size) return input;
        return String.valueOf(padChar).repeat(size - input.length()) + input;
    }

    public static String rightPad(String input, int size, char padChar) {
        if (input == null) return null;
        if (input.length() >= size) return input;
        return input + String.valueOf(padChar).repeat(size - input.length());
    }

    public byte[] convertBase64ToBytes(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            return null;
        }

        try {
            String cleanBase64 = base64String.trim();

            System.out.println("Before cleaning: " + cleanBase64); // DEBUG

            // Remove "data:image/jpeg;base64," prefix if present
            if (cleanBase64.startsWith("data:")) {
                int commaIndex = cleanBase64.indexOf(",");
                if (commaIndex == -1) {
                    System.out.println("No comma found in data URI"); // DEBUG
                    return null;
                }
                cleanBase64 = cleanBase64.substring(commaIndex + 1);
                System.out.println("After cleaning: " + cleanBase64.substring(0, Math.min(20, cleanBase64.length())) + "..."); // DEBUG
            }

            // Decode base64 to get actual image bytes
            byte[] result = Base64.getDecoder().decode(cleanBase64);
            System.out.println("Successfully decoded " + result.length + " bytes"); // DEBUG
            return result;

        } catch (IllegalArgumentException e) {
            System.out.println("Base64 decoding failed: " + e.getMessage()); // DEBUG
            return null;
        }
    }
}
