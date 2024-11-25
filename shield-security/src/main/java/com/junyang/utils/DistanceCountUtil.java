package com.junyang.utils;

import java.awt.geom.Path2D;
import java.util.List;

/**
 * @category 计算经纬度距离
 * @author csz
 *
 */
public class DistanceCountUtil {
	
	/**
	 * @c 使用Haversine公式计算两个经纬度之间的距离
	 * @param lat1 当前纬度
	 * @param lon1 当前经度
	 * @param lat2 定位纬度(固定位置)
	 * @param lon2 定位经度(固定位置)
	 * @return
	 */
	public static double calculateDistance(String latStr, String lonStr, String latStr2, String lonStr2) {
		Double lat1 = Double.parseDouble(latStr);
		Double lon1 = Double.parseDouble(lonStr);
		Double lat2 = Double.parseDouble(latStr2);
		Double lon2 = Double.parseDouble(lonStr2);
    	double EARTH_RADIUS = 6371.0;  // 地球半径，单位：千米
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
    
    /**
     * @category /使用射线法判断点是否在多边形内
     * @param lat 当前纬度
     * @param lon  当前经度
     * @param polyLat  定位纬度范围(固定位置)
     * @param polyLon 定位经度范围(固定位置)
     * @return
     */
    public static boolean isPointInPolygon(double lat, double lon, List<Double> polyLat, List<Double> polyLon) {
        Path2D path = new Path2D.Double();
        path.moveTo(polyLon.get(0), polyLat.get(0));

        for (int i = 1; i < polyLon.size(); i++) {
            path.lineTo(polyLon.get(i), polyLat.get(i));
        }

        path.closePath();

        return path.contains(lon, lat);
    }

}
