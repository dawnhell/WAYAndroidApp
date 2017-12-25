package way.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

import way.R;

public class VRActivity extends FragmentActivity {
    private VrPanoramaView vr_pan_view;
    private final String TAG = "VrPanoramaView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);
        load360Image();
    }

    private void load360Image() {
        vr_pan_view = (VrPanoramaView) findViewById(R.id.vr_pan_view);

        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + getIntent().getStringExtra("imageName"));

        VrPanoramaView.Options options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        vr_pan_view.setEventListener(new VrPanoramaEventListener() {
            @Override
            public void onDisplayModeChanged(int newDisplayMode) {
                super.onDisplayModeChanged(newDisplayMode);
                Log.d(TAG, "onDisplayModeChanged()->newDisplayMode=" + newDisplayMode);
            }

            @Override
            public void onLoadError(String errorMessage) {
                super.onLoadError(errorMessage);
                Log.d(TAG, "onLoadError()->errorMessage=" + errorMessage);
            }

            @Override
            public void onLoadSuccess() {
                super.onLoadSuccess();
                Log.d(TAG, "onLoadSuccess");
            }

            @Override
            public void onClick() {
                super.onClick();
                Log.d(TAG, "onClick()");
            }
        });

        vr_pan_view.loadImageFromBitmap(bitmap, options);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vr_pan_view.pauseRendering();
        vr_pan_view.shutdown();
    }
}