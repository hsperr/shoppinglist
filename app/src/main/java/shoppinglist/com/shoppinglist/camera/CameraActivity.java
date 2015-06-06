package shoppinglist.com.shoppinglist.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import shoppinglist.com.shoppinglist.R;

public class CameraActivity extends ActionBarActivity implements View.OnClickListener, Camera.ShutterCallback {

    private CameraPreviewSurface mSurface;
    private Camera mCamera;
    private Vibrator mVibrator; // LOL!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        ImageButton shutter = (ImageButton) findViewById(R.id.shutter);
        shutter.setOnClickListener(this);

        mSurface = new CameraPreviewSurface(this);
        ViewGroup surfaceContainer = (ViewGroup)findViewById(R.id.surface_container);
        surfaceContainer.removeAllViews();
        surfaceContainer.addView(mSurface);
    }

    @Override
    protected void onResume() {
        super.onResume();

        releaseCameraAndPreview();
        mCamera = CameraUtils.selectCamera(this, Camera.CameraInfo.CAMERA_FACING_BACK);
        if (mCamera != null) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
              //mCamera.autoFocus(new Camera.AutoFocusCallback() {
              //    @Override
              //    public void onAutoFocus(boolean success, Camera camera) {
              //        if (success) {
              //            mVibrator.vibrate(50);
              //        }
              //    }
              //});
            }

            mSurface.setCamera(mCamera);
        } else {
            new AlertDialog.Builder(this)
                .setMessage(R.string.error_camera_acquisition)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        return;
                    }
                })
                .create()
                .show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            menu.removeItem(R.id.action_torch);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            finish();
            return true;
        } else if (id == R.id.action_torch) {
            toggleTorch();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mCamera.takePicture(this, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // TODO Data is in JPEG format, save it somewhere and do stuff with it
                // TODO TODO can't assume that camera supports jpeg
            }
        });
    }

    @Override
    public void onShutter() {
        finish();
    }

    private void toggleTorch() {
        Camera.Parameters params = mCamera.getParameters();
        if (params.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        mCamera.setParameters(params);
    }

    private void releaseCameraAndPreview() {
        mSurface.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
