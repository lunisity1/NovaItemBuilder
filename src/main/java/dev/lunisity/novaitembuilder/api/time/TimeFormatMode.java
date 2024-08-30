package dev.lunisity.novaitembuilder.api.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
public enum TimeFormatMode {

    FULL(" months", " days", " hours", " minutes", " seconds"),
    SHORT(" mo", " d", " h", " m", " s");

    private final String months, days, hours, minutes, seconds;

    public String apply(final long time, final TimeUnit timeUnit) {

        if (time <= 0) {
            return "now";
        }

        final StringBuilder textJoiner = new StringBuilder(", ");

        final long milliseconds = timeUnit.toMillis(time);

        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        final long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24;
        final long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        final long months = days / 30;

        if (months > 0) {
            textJoiner.append(months).append(this.months);
        }

        if (days > 0) {
            textJoiner.append(days).append(this.days);
        }
        if (hours > 0) {
            textJoiner.append(hours).append(this.hours);
        }
        if (minutes > 0) {
            textJoiner.append(minutes).append(this.minutes);
        }
        if (seconds > 0) {
            textJoiner.append(seconds).append(this.seconds);
        }

        if (days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
            textJoiner.append("now");
        }
        return textJoiner.toString();
    }
}
