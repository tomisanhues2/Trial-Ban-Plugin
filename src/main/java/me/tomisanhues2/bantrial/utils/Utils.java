package me.tomisanhues2.bantrial.utils;

import me.tomisanhues2.bantrial.Ban;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private final Ban plugin;

    public Utils(Ban plugin) {
        this.plugin = plugin;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    @Nullable
    public BanType getBanType(String duration) {
        if (duration.equalsIgnoreCase("PERMANENT")) {
            return BanType.PERM_BAN;
        } else {
            return BanType.TEMP_BAN;
        }
    }

    public Date calculateUnbanDate(String duration) {
        if (this.getBanType(duration) == BanType.PERM_BAN)
            return null;
        Date currentDate = new Date();
        long currentDateMilli = currentDate.getTime();
        long addMilli = 0L;
        String[] var = duration.split("(?<=[smhd])");
        for (String v : var) {
            long var2 = Long.parseLong(v.substring(0, v.length() - 1));
            switch (v.charAt(v.length() - 1)) {
                case 's':
                    addMilli += var2 * 1000L;
                    break;
                case 'm':
                    addMilli += var2 * 60000L;
                    break;
                case 'h':
                    addMilli += var2 * 3600000L;
                    break;
                case 'd':
                    addMilli += var2 * 86400000L;
                    break;
            }
        }
        return new Date(currentDateMilli + addMilli);
    }
}
