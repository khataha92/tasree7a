package com.tasree7a.utils;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid Taha on 3/17/16.
 * Implements the most used methods
 */

public class MapsUtils {


    public static void addSimpleMarkerToMap(Location location, IconGenerator mDetailsIconGenerator, GoogleMap mMap){

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(UIUtils.dpToPx(24), UIUtils.dpToPx(32));

        ImageView imageView = new ImageView(ThisApplication.getCurrentActivity());

        imageView.setId(com.google.maps.android.R.id.amu_text);

        imageView.setImageResource(R.drawable.ic_mini_salon_marker);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setLayoutParams(layoutParams);


        mDetailsIconGenerator.setContentView(imageView);

        mDetailsIconGenerator.setBackground(ContextCompat.getDrawable(ThisApplication.getCurrentActivity(), R.drawable.ic_mini_salon_marker));

        Bitmap icon = mDetailsIconGenerator.makeIcon();

        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 1.0f)
                .position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    public static String generateStaticMapUrl(double lat, double lng, int width, int height) {

        return "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng
                + "&zoom=15&size=" + width + "x" + (height)
                + "&sensor=false"
                + "&markers=anchor:0.5,1|icon:http://www.onfi.org.uy/onfi_mixto/media/contacts/images/con_address.png|"+"backgroundColor:blue|" + lat + "," + lng;

    }


    /**
     * If want to get zoom level for LatLngBounds in map
     */
    public static int getBoundsZoomLevel(LatLngBounds bounds, int mapHeightPx, int mapWidthPx, int ZOOM_MAX){

        final int WORLD_PX_HEIGHT = 256;

        final int WORLD_PX_WIDTH = 256;

        LatLng ne = bounds.northeast;

        LatLng sw = bounds.southwest;

        double latFraction = (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI;

        double lngDiff = ne.longitude - sw.longitude;

        double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;

        double latZoom = zoom(mapHeightPx, WORLD_PX_HEIGHT, latFraction);

        double lngZoom = zoom(mapWidthPx, WORLD_PX_WIDTH, lngFraction);

        int result = Math.min((int)latZoom, (int)lngZoom);

        return Math.min(result, ZOOM_MAX);
    }

    public static void removeFragmentIfAlreadyLoaded(int mapResourceId) {

        if (ThisApplication.getCurrentActivity() != null) {

            try {

                android.app.FragmentManager fm = ThisApplication.getCurrentActivity().getFragmentManager();

                MapFragment fragment = ((MapFragment) fm.findFragmentById(mapResourceId));

                if (fragment != null) {

                    fragment.onDestroyView();

                    FragmentTransaction ft = fm.beginTransaction();

                    ft.remove(fragment);

                    ft.commitAllowingStateLoss();

                }
            } catch (Throwable t) {

                Log.e("HotelsMapView", "Error in removing fragment ", t);

            }
        }

    }

    private static double latRad(double lat) {

        double sin = Math.sin(lat * Math.PI / 180);

        double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;

        return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;

    }


    private static double zoom(int mapPx, int worldPx, double fraction) {

        final double LN2 = 0.6931471805599453;

        return Math.floor(Math.log(mapPx / worldPx / fraction) / LN2);

    }


    /**
     * Get LatLngBounds of circle given raduis in meters and center point
     * @param center LatLng
     * @param radius double
     * @return LatLngBounds
     */
    public static LatLngBounds cirlceToBounds(LatLng center, double radius) {

        return new LatLngBounds.Builder().
                include(SphericalUtil.computeOffset(center, radius, 0)).
                include(SphericalUtil.computeOffset(center, radius, 90)).
                include(SphericalUtil.computeOffset(center, radius, 180)).
                include(SphericalUtil.computeOffset(center, radius, 270)).build();
    }


    // Enlarge LatLng by percentage
    public static LatLngBounds increaseLatLngBoundsArea(Projection mapProjection, float byPercent) {

        LatLngBounds bounds = mapProjection.getVisibleRegion().latLngBounds;

        Point southPoint = mapProjection.toScreenLocation(bounds.southwest);

        Point northPoint = mapProjection.toScreenLocation(bounds.northeast);

        Point centerPoint = mapProjection.toScreenLocation(bounds.getCenter());


        // calculate new northeast
        int newXNorth = centerPoint.x + (int)(Math.abs(northPoint.x - centerPoint.x) * byPercent);

        int newYNorth = centerPoint.y - (int)(Math.abs(northPoint.y - centerPoint.y) * byPercent) ;

        Point newNorthEast = new Point(newXNorth, newYNorth);


        // calculate new southwest
        int newXSouth = centerPoint.x - (int)(Math.abs(southPoint.x - centerPoint.x) * byPercent);

        int newYSouth = centerPoint.y + (int)(Math.abs(southPoint.y - centerPoint.y) * byPercent) ;

        Point newSouthWest = new Point(newXSouth, newYSouth);

        return new LatLngBounds.Builder()
                .include(mapProjection.fromScreenLocation(newNorthEast))
                .include(mapProjection.fromScreenLocation(newSouthWest)).build();

    }

    /**
     * Determine should this marker be rendered? rendered if it in the visible area of the map and
     *  if it lie in the distance bounds if the distance bounds determined
     * @param centerPosition LatLng
     * @param markerPosition LatLng
     * @param maxDistance int in meters
     * @return boolean
     */
    public static boolean isWithinAllowedDistance(LatLng centerPosition, LatLng markerPosition, int maxDistance) {

        float[] distanceArr = new float[1];

        Location.distanceBetween(centerPosition.latitude, centerPosition.longitude,
                markerPosition.latitude,markerPosition.longitude
                , distanceArr);

        int distance = (int) distanceArr[0];

        return distance <= maxDistance;

    }
//
//    public static int metersToRadius(float meters, GoogleMap map, double latitude) {
//        return (int) (map.getProjection().metersToEquatorPixels(meters) * (1/ Math.cos(Math.toRadians(latitude))));
//    }

    /**
     * Converts meters to pixels approximately
     * @param map GoogleMap
     * @param base LatLng
     * @param meters float
     * @return int pixels
     */
    public static int metersToEquatorPixels(GoogleMap map, LatLng base, float meters) {

        Projection proj = map.getProjection();

        Point basePt = proj.toScreenLocation(base);

        Point destPt = proj.toScreenLocation(SphericalUtil.computeOffset(base, meters, 90));

        return Math.abs(destPt.x - basePt.x);
    }

    public static boolean isLocationInBounds(LatLng location, LatLngBounds bounds) {

        if (bounds == null || location == null) return false;

        return bounds.contains(location);

    }


    // Decode polyline string from google directions api to list of LatLng
    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


}
