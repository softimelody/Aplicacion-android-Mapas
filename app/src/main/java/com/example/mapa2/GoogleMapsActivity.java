package com.example.mapa2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "GoogleMapsActivity";
    private GoogleMap mMap;
    private Marker selectedMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // Puntos de Interés (POIs) en La Florida
    private static final LatLng MINDFIT_GYM_LOCATION = new LatLng(-33.519701, -70.599117);
    private static final LatLng MALL_VESPUCIO_LOCATION = new LatLng(-33.518605, -70.600223);
    private static final LatLng CAMERA_CENTER = new LatLng(-33.521385, -70.598241);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_maps);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verificar si Google Play Services está disponible
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                android.app.Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, 9000);
                if (dialog != null) {
                    dialog.show();
                }
            } else {
                Toast.makeText(this, "Google Play Services no está soportado en este dispositivo", Toast.LENGTH_LONG).show();
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "SupportMapFragment es NULL");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "GoogleMap está listo para usarse");

        // Configurar tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Habilitar controles de la interfaz
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Centrar la cámara entre los dos puntos de interés con zoom adecuado
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CAMERA_CENTER, 16f));

        // Agregar los Puntos de Interés
        agregarPuntosDeInteres();

        // Configurar listener para seleccionar puntos al presionar sobre el mapa
        mMap.setOnMapClickListener(latLng -> {
            Log.d(TAG, "Punto seleccionado: Lat " + latLng.latitude + " Lon " + latLng.longitude);

            // Eliminar marcador previamente seleccionado si existe
            if (selectedMarker != null) {
                selectedMarker.remove();
            }

            // Crear nuevo marcador en el punto presionado
            selectedMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("ACA")
                    .snippet(String.format(Locale.getDefault(), "Lat: %.6f, Lon: %.6f", latLng.latitude, latLng.longitude))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            if (selectedMarker != null) {
                selectedMarker.showInfoWindow();
            }
        });

        // Habilitar mi ubicación y geolocalización
        enableMyLocation();
    }

    private void agregarPuntosDeInteres() {
        // Punto de Interés 1: Gimnasio MindFit
        mMap.addMarker(new MarkerOptions()
                .position(MINDFIT_GYM_LOCATION)
                .title(getString(R.string.poi_mindfit_title))
                .snippet(getString(R.string.poi_mindfit_snippet))
                .icon(createMarkerIconFromResource(this, R.drawable.mindift, 60, 40)));

        // Punto de Interés 2: Mallplaza Vespucio
        mMap.addMarker(new MarkerOptions()
                .position(MALL_VESPUCIO_LOCATION)
                .title(getString(R.string.poi_mall_title))
                .snippet(getString(R.string.poi_mall_snippet))
                .icon(createMarkerIconFromResource(this, R.drawable.mall, 60, 60)));
    }

    /**
     * Convierte cualquier recurso de imagen (PNG, WebP, JPG o Vector) en un BitmapDescriptor
     * redimensionado automáticamente a las dimensiones en DP especificadas.
     */
    private BitmapDescriptor createMarkerIconFromResource(Context context, @DrawableRes int resId, int widthDp, int heightDp) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        if (drawable == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        float density = context.getResources().getDisplayMetrics().density;
        int widthPx = Math.round(widthDp * density);
        int heightPx = Math.round(heightDp * density);

        Bitmap bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, widthPx, heightPx);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16f));
                }
            }).addOnFailureListener(this, e -> Log.e(TAG, "Error obteniendo ubicación: " + e.getMessage()));

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}