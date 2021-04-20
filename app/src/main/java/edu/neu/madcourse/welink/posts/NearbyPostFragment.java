package edu.neu.madcourse.welink.posts;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.Formatter;

public class NearbyPostFragment extends Fragment implements LocationListener {
    private String currUID;
    private LocationManager locationManager;
    private Location location;
    private String locationString;
    RecyclerView.LayoutManager layoutManger;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    androidx.appcompat.app.AlertDialog.Builder gpsAlertBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            currUID = bundle.getString("currUID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return inflater.inflate(R.layout.posts_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.post_rv);
        recyclerView.setHasFixedSize(true);
    }

    private void bindAdapterToRecycleView() {
        postAdapter = new PostAdapter(currUID, "nearby", locationString);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String newLocationString = Formatter.formateLocationString(location.getLatitude(), location.getLongitude());
        if(locationString == null || !locationString.equals(newLocationString)) {
            locationString = newLocationString;
            setLocationString();
        }
    }

//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//        if(getActivity() != null) {
//            setLocationString();
//        }
//    }
//
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if(getActivity() != null) {
            Toast.makeText(getActivity(), "Require GPS service to see nearby posts.", Toast.LENGTH_SHORT).show();
            ((DeleteFragmentCallBack) getActivity()).backToPreviousFragment();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if(isGranted) {
                    getGPSPermission();
                    setLocationString();
                } else {
                    Toast.makeText(getActivity(), "Require Location service to see nearby posts.", Toast.LENGTH_SHORT).show();
                    ((DeleteFragmentCallBack) getActivity()).backToPreviousFragment();
                }
            });


    private void getLocationPermission() {
        boolean canGetLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!canGetLocation) {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getGPSPermission();
        }
    }

    private void getGPSPermission() {
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isGPSEnable) {
            if(gpsAlertBuilder == null) {
                gpsAlertBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                gpsAlertBuilder.setMessage("GPS not enabled.")
                        .setCancelable(false)
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(getActivity(), "Require GPS service to see nearby posts.", Toast.LENGTH_SHORT).show();
                                ((DeleteFragmentCallBack) getActivity()).backToPreviousFragment();
                            }
                        });
                gpsAlertBuilder.create();
                gpsAlertBuilder.show();
            }
        }
    }

    private void setLocationString() {
        boolean canGetLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable && canGetLocation ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    locationString = Formatter.formateLocationString(location.getLatitude(), location.getLongitude());
                    bindAdapterToRecycleView();
                }
            }
        }
//      else {
//            Toast.makeText(getActivity(), "Require location and GPS service to see nearby posts.", Toast.LENGTH_SHORT).show();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_LOCATION_CODE) {
//            setLocationString();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null) {
            getLocationPermission();
            setLocationString();
        }
    }

    public interface DeleteFragmentCallBack {
        void backToPreviousFragment();
    }
}
