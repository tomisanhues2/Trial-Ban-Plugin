package me.tomisanhues2.bantrial.utils;

import me.tomisanhues2.bantrial.Ban;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Utils {
    private final Ban plugin;

    public Utils(Ban plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public BanType getBanType(String duration) {
        if (duration.equalsIgnoreCase("PERMANENT")) {
            return BanType.PERM_BAN;
        } else {
            return BanType.TEMP_BAN;
        }
    }

    public Date calculateUnbanDate(String duration) {
        Date date = new Date();
        return calculateUnbanDate(date, duration);
    }

    public Date calculateUnbanDate(Date date, String duration) {
        if (this.getBanType(duration) == BanType.PERM_BAN)
            return new Date(date.getTime() + 1000000000000000000L);
        long currentDate = date.getTime();
        Duration duration1 = parseDurationString(duration);
        long unbanDate = currentDate + duration1.toMillis();
        return new Date(unbanDate);
    }

    private Duration parseDurationString(String durationString) {
        Duration duration = Duration.ZERO;

        int pos = 0;
        while (pos < durationString.length()) {
            int num = 0;
            while (pos < durationString.length() && Character.isDigit(durationString.charAt(pos))) {
                num = num * 10 + (durationString.charAt(pos) - '0');
                pos++;
            }
            char unit = durationString.charAt(pos);
            pos++;

            duration = switch (unit) {
                case 's' -> duration.plusSeconds(num);
                case 'm' -> duration.plusMinutes(num);
                case 'h' -> duration.plusHours(num);
                case 'd' -> duration.plusDays(num);
                case 'w' -> duration.plus(num, ChronoUnit.WEEKS);
                case 'o' -> duration.plus(num * 30, ChronoUnit.DAYS);
                default -> throw new IllegalArgumentException("Unknown unit: " + unit);
            };
        }
        return duration;
    }

}
