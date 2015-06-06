package shoppinglist.com.shoppinglist.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class CameraPreviewSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private List<Camera.Size> mPreviewSizes;
    private SurfaceHolder mHolder;

    public CameraPreviewSurface(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters params = mCamera.getParameters();
        Camera.Size previewSize = selectOptimumPreviewSize(mPreviewSizes);
        params.setPreviewSize(previewSize.width, previewSize.height);
        requestLayout();
        mCamera.setParameters(params);

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) return;

        stopPreviewAndFreeCamera();
        mCamera = camera;

        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            mPreviewSizes = params.getSupportedPreviewSizes();
            requestLayout();

            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);
        }
    }

    /**
     * @param sizeList list of supported preview sizes for the camera
     * @return gets the smallest preview size that is
     *           1) larger than the preview surface
     *           2) has the closes aspect ratio to the preview surface
     *
     *           If no suitable size is found, just pick the first. Meh~
     */
    public Camera.Size selectOptimumPreviewSize(final List<Camera.Size> sizeList) {
        TreeMap<Float, TreeSet<Camera.Size>> aspectMapping = new TreeMap<>();
        Iterator<Camera.Size> it = sizeList.iterator();
        int w = getWidth(),
            h = getHeight();
        float aspectRatio = (float)h / w;

        while (it.hasNext()) {
            Camera.Size size = it.next();

            // we're snobs, nothing smaller!
            if (size.width < w || size.height < h)
                continue;

            float ar = size.width / size.height;
            float arDelta = Math.abs(ar - aspectRatio);
            TreeSet<Camera.Size> aspectGroup = aspectMapping.get(arDelta);

            if (aspectGroup == null) {
                aspectGroup = new TreeSet<>();
            }

            aspectGroup.add(size);
            aspectMapping.put(arDelta, aspectGroup);
        }

        Map.Entry<Float, TreeSet<Camera.Size>> least = aspectMapping.firstEntry();
        return least != null ? least.getValue().first() : sizeList.get(0);
    }

    public void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
