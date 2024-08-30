package dev.lunisity.novaitembuilder.api.color;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorAPI {

    private static final Pattern THREE_COLOR_GRADIENT_PATTERN = Pattern.compile("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})(:bold)?>((.*?)</gradient>)");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})(:bold)?>((.*?)</gradient>)");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static List<String> apply(List<String> messages) {
        messages.forEach(ColorAPI::apply);
        return messages;
    }

    public static String apply(String message) {
        // Check if the message matches the 3-color gradient pattern
        Matcher threeColorMatcher = THREE_COLOR_GRADIENT_PATTERN.matcher(message);
        if (threeColorMatcher.find()) {
            return applyGradients(message);
        }

        // Check if the message matches the 2-color gradient pattern
        Matcher twoColorMatcher = GRADIENT_PATTERN.matcher(message);
        if (twoColorMatcher.find()) {
            return applyGradient(message);
        }

        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        if (hexMatcher.find()) {
            return hex(message);
        }

        // If no gradient pattern found, translate alternate color codes
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String applyGradients(String message) {
        Matcher matcher = THREE_COLOR_GRADIENT_PATTERN.matcher(message);
        while (matcher.find()) {
            String startColor = matcher.group(1);
            String middleColor = matcher.group(2);
            String endColor = matcher.group(3);
            boolean isBold = matcher.group(4) != null;
            String text = matcher.group(6);

            String gradientText = createThreeColorGradient(text, Color.decode(startColor), Color.decode(middleColor), Color.decode(endColor), isBold);
            message = message.replace(matcher.group(), gradientText);
        }
        return message;
    }

    private static String createThreeColorGradient(String text, Color startColor, Color middleColor, Color endColor, boolean isBold) {
        StringBuilder result = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (float) (length - 1);
            Color color;
            if (ratio < 0.5) {
                float subRatio = ratio * 2;
                int red = (int) (startColor.getRed() * (1 - subRatio) + middleColor.getRed() * subRatio);
                int green = (int) (startColor.getGreen() * (1 - subRatio) + middleColor.getGreen() * subRatio);
                int blue = (int) (startColor.getBlue() * (1 - subRatio) + middleColor.getBlue() * subRatio);
                color = new Color(red, green, blue);
            } else {
                float subRatio = (ratio - 0.5f) * 2;
                int red = (int) (middleColor.getRed() * (1 - subRatio) + endColor.getRed() * subRatio);
                int green = (int) (middleColor.getGreen() * (1 - subRatio) + endColor.getGreen() * subRatio);
                int blue = (int) (middleColor.getBlue() * (1 - subRatio) + endColor.getBlue() * subRatio);
                color = new Color(red, green, blue);
            }
            ChatColor chatColor = ChatColor.of(color);
            result.append(chatColor);
            if (isBold) {
                result.append(ChatColor.BOLD);
            }
            result.append(text.charAt(i));
        }
        return result.toString();
    }

    public static String applyGradient(String message) {
        Matcher matcher = GRADIENT_PATTERN.matcher(message);
        while (matcher.find()) {
            String startColor = matcher.group(1);
            String endColor = matcher.group(2);
            boolean isBold = matcher.group(3) != null;
            String text = matcher.group(5);

            String gradientText = createGradient(text, Color.decode(startColor), Color.decode(endColor), isBold);
            message = message.replace(matcher.group(), gradientText);
        }
        return message;
    }

    private static String createGradient(String text, Color startColor, Color endColor, boolean isBold) {
        StringBuilder result = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (float) (length - 1);
            int red = (int) (startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
            int green = (int) (startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
            int blue = (int) (startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);
            ChatColor color = ChatColor.of(new Color(red, green, blue));
            result.append(color);
            if (isBold) {
                result.append(ChatColor.BOLD);
            }
            result.append(text.charAt(i));
        }
        return result.toString();
    }

    public static String hex(String input) {
        Matcher match = HEX_PATTERN.matcher(input);
        while(match.find()){
            String color = input.substring(match.start()+1, match.end());
            input = input.replace("&" + color, ChatColor.of(color) + "");
            match = HEX_PATTERN.matcher(input);
        }
        return input.replace("&", "\u00a7");
    }


}
