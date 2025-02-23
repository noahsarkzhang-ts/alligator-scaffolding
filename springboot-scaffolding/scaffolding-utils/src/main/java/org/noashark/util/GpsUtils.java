package org.noashark.util;

/**
 * GPS 工具类
 *
 * @author zhangxt
 * @date 2024/06/03 17:44
 **/
public class GpsUtils {

    private static final double EARTH_RADIUS = 6378137;//赤道半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double gpsDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;//单位米
    }

    public static void main(String[] args) {
        double lon1 = 125.1469310805;
        double lat1 = 44.4517388006;

        double lon2 = 125.1747914528;
        double lat2 = 44.4557186755;

        double distance = gpsDistance(lon1,lat1,lon2,lat2);

        System.out.println("distance = " + distance);
    }
}
