package shoppinglist.com.shoppinglist.camera;


import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

public class CameraUtils {
    public static final String TAG = CameraUtils.class.getSimpleName();

    /**
     * Open and initialize a camera.
     *
     * @param activity activity
     * @param facing Desired camera facing orientation. Valid values are
     *               Camera.CameraInfo.CAMERA_FACING_BACK
     *               Camera.CameraInfo.CAMERA_FACING_FRONT
     * @return returns a camera instance. If no matching camera is found, returns null.
     */
    public static Camera selectCamera(Activity activity, int facing) {
        if (facing != Camera.CameraInfo.CAMERA_FACING_BACK &&
                facing != Camera.CameraInfo.CAMERA_FACING_FRONT) {
            throw new IllegalArgumentException("argument 'facing' must either CAMERA_FACING_BACK or CAMERA_FACING_FRONT");
        }

        Camera camera = null;

        int camCount = Camera.getNumberOfCameras();
        for (int i = 0; i < camCount; ++i) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == facing) {
                try {
                    camera = Camera.open(i);
                    setCameraDisplayOrientation(activity, i, camera);
                    break;
                } catch (RuntimeException e) {
                    // Something prevented camera acquisition, keep looking
                    Log.e(TAG, e.getMessage());
                    continue;
                }
            }
        }

        return camera;
    }

    static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
