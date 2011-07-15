package com.orange.common.utils.geohash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProximitySearchUtil {

	private static Logger log = LoggerFactory
			.getLogger(ProximitySearchUtil.class);

	private static final int DEFAULT_PRECISION = 7;
	private static final double EARTH_RADIUS = 6378137;
	private static final double RAD = Math.PI / 180.0;
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	private static final int[] directions = new int[] { NORTH, EAST, SOUTH,
			WEST };

	private final static String even_neighbors[] = new String[] {
			"p0r21436x8zb9dcf5h7kjnmqesgutwvy",
			"bc01fg45238967deuvhjyznpkmstqrwx",
			"14365h7k9dcfesgujnmqp0r2twvyx8zb",
			"238967debc01fg45kmstqrwxuvhjyznp" };

	private final static String odd_neighbors[] = {
			"bc01fg45238967deuvhjyznpkmstqrwx",
			"p0r21436x8zb9dcf5h7kjnmqesgutwvy",
			"238967debc01fg45kmstqrwxuvhjyznp",
			"14365h7k9dcfesgujnmqp0r2twvyx8zb" };

	private final static String even_borders[] = { "prxz", "bcfguvyz", "028b",
			"0145hjnp" };
	private final static String odd_borders[] = { "bcfguvyz", "prxz",
			"0145hjnp", "028b" };

	private int precision;

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public ProximitySearchUtil() {
		this.precision = DEFAULT_PRECISION;
	}

	public ProximitySearchUtil(int precision) {
		this.precision = precision;
	}

	public List<String> getNearBy(double latitude, double longitude,
			double radius) {
		Set<String> resultSet = new HashSet<String>();

		GeoHashUtil util = getGeoHashUtil();
		String sourcePoint = util.encode(latitude, longitude);

		List<String> candidates = new ArrayList<String>();
		candidates.add(sourcePoint);
		resultSet.add(sourcePoint);

		Set<String> processedSet = new HashSet<String>();
		while (!candidates.isEmpty()) {
			String candidate = candidates.remove(0);
			// if candidate suitable, add to result set
			if (isGeohashInRange(latitude, longitude, radius, candidate)) {
				// if (!resultSet.contains(candidate)) {
				// add to result
				resultSet.add(candidate);
				// move neighbor to candidate, if it does't exist in result
				for (int direction : directions) {
					String neighbor = getNeighbor(candidate, direction);
					if (!resultSet.contains(neighbor)
							&& !candidates.contains(neighbor)
							&& !processedSet.contains(neighbor)) {
						candidates.add(neighbor);
					}
				}
				// }
			}
			// have calculated
			processedSet.add(candidate);
		}
		return convertToSortList(resultSet);
	}

	public boolean isGeohashInRange(double latitude, double longitude,
			double radius, String geohash) {
		GeoHashUtil util = getGeoHashUtil();
		double[] location = util.decode(geohash);

		double distance = getDistance(latitude, longitude, location[0],
				location[1]);
		log.debug(
				"distance={}, radius={} ; latitude={}, longitude={}; location[0]={}, location[1]={}",
				new Object[] { distance, radius, latitude, longitude,
						location[0], location[1] });
		return distance <= radius;
	}

	public double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = lat1 * RAD;
		double radLat2 = lat2 * RAD;
		double a = radLat1 - radLat2;
		double b = (lng1 - lng2) * RAD;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private GeoHashUtil getGeoHashUtil() {
		GeoHashUtil util = new GeoHashUtil();
		util.setPrecision(precision);
		return util;
	}

	private List<String> convertToSortList(Set<String> resultSet) {
		List<String> result = new ArrayList<String>();
		Iterator<String> it = resultSet.iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		Collections.sort(result);
		return result;
	}

	// change from here
	public String getNeighbor(String hash, int direction) {
		int hash_length = hash.length();
		char last_char = hash.charAt(hash_length - 1);

		boolean is_odd = hash_length % 2 == 1 ? true : false;
		String[] border = is_odd ? odd_borders : even_borders;
		String[] neighbor = is_odd ? odd_neighbors : even_neighbors;

		String base = hash.substring(0, hash_length - 1);
		if (border[direction].indexOf(last_char) != -1) {
			base = getNeighbor(base, direction);
		}

		int neighbor_index = neighbor[direction].indexOf(last_char);
		last_char = GeoHashUtil._base32[neighbor_index];

		base = base + String.valueOf(last_char);
		return base;
	}
}