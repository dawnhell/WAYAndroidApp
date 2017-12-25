package way.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import way.R;
import way.activities.VRActivity;
import way.services.HTTPService;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    LinearLayout imageLayout;
    Button       takePhotoBtn;
    Button       createVRPhotoBtn;
    TextView     remainingText;
    Bitmap       clippedImageBitmap;
    String       fileName;

    ArrayList<Bitmap> imageBitmapList = new ArrayList<Bitmap>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageLayout      = (LinearLayout) view.findViewById(R.id.imageLayout);
        takePhotoBtn     = (Button)       view.findViewById(R.id.takePhotoBtn);
        createVRPhotoBtn = (Button)       view.findViewById(R.id.createVRBtn);
        remainingText    = (TextView)     view.findViewById(R.id.remainingText);

        createVRPhotoBtn.setEnabled(false);
        remainingText.setText("You need 6 more photos to be able to create a VR Photo");

        this.bindButtonClicks();

        return view;
    }

    public void bindButtonClicks() {
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                dispatchTakePictureIntent();
            }
        });

        createVRPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                clippedImageBitmap = Bitmap.createBitmap(imageBitmapList.get(0).getWidth(), imageBitmapList.get(0).getWidth(), Bitmap.Config.ARGB_8888);

                for (int i = 0 ; i < imageBitmapList.size(); ++i) {
                    clippedImageBitmap = combineImagesHorizontally(clippedImageBitmap, imageBitmapList.get(i));
                }

                clippedImageBitmap = combineImagesVertically(clippedImageBitmap, clippedImageBitmap);

                savePhotoToFile();
                imageBitmapList = new ArrayList<Bitmap>();
                createVRPhotoBtn.setEnabled(false);
                imageLayout.removeAllViews();
                remainingText.setText("You need 6 more photos to be able to create a VR Photo");

                Intent intent = new Intent(getContext(), VRActivity.class);
                intent.putExtra("imageName", fileName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(imageBitmap);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
            imageView.setLayoutParams(layoutParams);

            imageLayout.addView(imageView);
            imageBitmapList.add(imageBitmap);

            createVRPhotoBtn.setEnabled(imageBitmapList.size() > 5);

            if (imageBitmapList.size() == 6) {
                remainingText.setText("You can now create VR photo!");
            } else {
                remainingText.setText("You need " + (6 - imageBitmapList.size()) + " more photos to be able to create a VR Photo");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void savePhotoToFile() {
        fileName = "WAY_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, fileName);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            clippedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String response = new HTTPService().sendVRPhoto(clippedImageBitmap);
            Log.i("Saving photo to server", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap combineImagesHorizontally(Bitmap c, Bitmap s) {
        Bitmap cs = null;
        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);

        return cs;
    }

    public Bitmap combineImagesVertically(Bitmap c, Bitmap s) {
        Bitmap cs = null;
        cs = Bitmap.createBitmap(c.getWidth(), 2 * c.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);

        return cs;
    }
}
