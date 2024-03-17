package org.toannguyen;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String generatePassword() {
        int length = 5;
        String upperCaseLetters = RandomStringUtils.random(length, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(length, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(length);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }
}
