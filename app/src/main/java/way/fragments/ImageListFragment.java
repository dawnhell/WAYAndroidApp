package way.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import way.R;
import way.activities.VRActivity;

public class ImageListFragment extends Fragment {
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        final View temp = view;

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                renderImages(temp);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        renderImages(view);

        return view;
    }

    public void renderImages(View view) {
        linearLayout.removeAllViews();
        File parentDir = new File(Environment.getExternalStorageDirectory().toString());

        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.getName().startsWith("WAY")) {
                inFiles.add(file);
                final File temp = file;

                ImageView imageView = new ImageView(getActivity());
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view) {
                        Intent intent = new Intent(getContext(), VRActivity.class);
                        intent.putExtra("imageName", temp.getName());
                        startActivity(intent);
                    }
                });

                linearLayout.addView(imageView);
            }
        }
    }
}
