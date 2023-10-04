package dev.ohate.lime.util;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Duration {

    public static final int INVALID = -2;
    public static final int PERMANENT = -1;

    private static final Pattern PATTERN = Pattern.compile("\\d+\\D+");

    private final long value;

    private Duration(long value) {
        this.value = value;
    }

    public static Duration fromString(String source) {
        if (source.equalsIgnoreCase("perm") || source.equalsIgnoreCase("permanent")) {
            return new Duration(PERMANENT);
        }

        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = PATTERN.matcher(source);

        while (matcher.find()) {
            String group = matcher.group();
            long value = Long.parseLong(group.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = group.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

            switch (type) {
                case "s" -> {
                    totalTime += value;
                    found = true;
                }
                case "m" -> {
                    totalTime += value * 60;
                    found = true;
                }
                case "h" -> {
                    totalTime += value * 60 * 60;
                    found = true;
                }
                case "d" -> {
                    totalTime += value * 60 * 60 * 24;
                    found = true;
                }
                case "w" -> {
                    totalTime += value * 60 * 60 * 24 * 7;
                    found = true;
                }
                case "M" -> {
                    totalTime += value * 60 * 60 * 24 * 30;
                    found = true;
                }
                case "y" -> {
                    totalTime += value * 60 * 60 * 24 * 365;
                    found = true;
                }
            }
        }

        return new Duration(!found || totalTime < 0 ? INVALID : totalTime);
    }

    public boolean isInvalid() {
        return value == INVALID;
    }

    public boolean isPermanent() {
        return value == PERMANENT;
    }

}
