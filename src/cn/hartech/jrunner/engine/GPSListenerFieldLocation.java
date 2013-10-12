package cn.hartech.jrunner.engine;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import cn.hartech.jrunner.FieldEditorActivity;

public class GPSListenerFieldLocation implements LocationListener {

	private FieldEditorActivity fieldEditorActivity;

	public GPSListenerFieldLocation(FieldEditorActivity fieldEditorActivity) {

		this.fieldEditorActivity = fieldEditorActivity;
	}

	@Override
	public void onLocationChanged(Location location) {

		fieldEditorActivity.updateLocation(location);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
