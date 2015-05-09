package shoppinglist.com.shoppinglist.camera;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;

import shoppinglist.com.shoppinglist.R;

public class CameraActivity extends ActionBarActivity implements View.OnClickListener, Camera.ShutterCallback {

    private ImageButton mShutter;
    private CameraPreviewSurfaceView mSurface;
    private Camera mCamera;
    private Vibrator mVibrator; // LOL!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mSurface = (CameraPreviewSurfaceView)findViewById(R.id.surface);
        mShutter = (ImageButton) findViewById(R.id.shutter);

        mShutter.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = Camera.open();
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mVibrator.vibrate(50);
                }
            }
        });

        mSurface.setCamera(mCamera);
        mSurface.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            // Shoudn't need to check, but maybe some nasty app didn't release
            mSurface.stopPreview();
            mCamera.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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

    private void toggleTorch() {
        Camera.Parameters params = mCamera.getParameters();
        if (params.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        mCamera.setParameters(params);
    }

    @Override
    public void onClick(View v) {
        mCamera.takePicture(this, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // TODO Data is in JPEG format, save it somewhere and do stuff with it
            }
        });
    }

    @Override
    public void onShutter() {
        finish();
    }
}
