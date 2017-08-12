package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pdepu on 12/08/2017.
 */

public class PublishRestaurantFragment extends Fragment {
    private String restaurantName;
    private String restaurantPoints;
    private static final int PICK_IMAGE = 0;

    @BindView(R.id.restaurantNamePublish)
    TextView restaurantNamePublishTextView;

    @BindView(R.id.restaurantPointsPublish)
    TextView restaurantPointsPublishTextView;

    @BindView(R.id.restaurantImage)
    ImageView restaurantImageView;

    @BindView(R.id.publishRestaurantButton)
    Button publishRestaurantButton;

    @BindView(R.id.uploadRestaurantImageButton)
    Button uploadRestaurantImageButton;

    private UploadImageTask uploadImageTask;
    private Map response;
    private Challenge challenge;

    private Service service;
    private File file;
    private String uri;
    private String imageUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantName = getArguments().getString("restaurantName");
            restaurantPoints = getArguments().getString("restaurantPoints");
        }
        service = new ServicesInitializer().initializeService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_publish_restaurant, container, false);
        ButterKnife.bind(this, rootView);
        updateView();
        return rootView;
    }

    @OnClick(R.id.uploadRestaurantImageButton)
    public void onClick() {
        pickImage();
    }


    @OnClick(R.id.publishRestaurantButton)
    public void onClickPublish() {
        if (file != null && uri != null)
            uploadImage();
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = getPath(getContext(), data.getData());
            file = new File(path);
            uri = path;
            Picasso.with(getActivity()).load(file).fit().into(restaurantImageView);
        }
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void uploadImage() {
        uploadImageTask = new UploadImageTask(file.getAbsolutePath());
        uploadImageTask.execute((Void) null);
    }


    private void updateView() {
        restaurantNamePublishTextView.setText("You went to " + restaurantName);
        restaurantPointsPublishTextView.setText("+" + restaurantPoints + " points" + "\n");
    }

    private void callApi() {
        MainActivity activity = (MainActivity)getActivity();
        Call<User> challengeCall = service.postChallenge(activity.token.getUserid(), challenge);
        challengeCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful())
                    Log.i("SUCCESS", "challenge posted to user");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("FAILURE", t.toString());
            }
        });
    }

    public class UploadImageTask extends AsyncTask<Void, Void, Boolean> {

        private final String filePath;

        UploadImageTask(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Map config = new HashMap();
            config.put("cloud_name", "douskchks");
            config.put("api_key", "552883666323728");
            config.put("api_secret", "RhDl-TvAXIiaPkeBWOHY8OcCwr8");
            final Cloudinary cloudinary = new Cloudinary(config);
            try {
                response = cloudinary.uploader().upload(file.getAbsolutePath(), ObjectUtils.emptyMap());
                if (response != null){
                    imageUrl = response.get("url").toString();
                    return true;
                }
                else
                    return false;
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                challenge = new Challenge("RecipeChallenge", restaurantName, imageUrl, Calendar.getInstance().getTime(), 0, Integer.parseInt(restaurantPoints), true);
                callApi();
            } else {


            }
        }

        @Override
        protected void onCancelled() {
            uploadImageTask = null;
        }


    }
}
