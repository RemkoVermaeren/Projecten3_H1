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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Challenge;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.ProfileChallengeAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pdepu on 8/08/2017.
 */

public class ProfileFragment extends Fragment {
    private Service service;
    private Token token;
    private User user;

    @BindView(R.id.imageViewUserProfile)
    ImageView imageViewUserProfile;

    @BindView(R.id.usernameProfile)
    TextView textViewUsernameProfile;

    @BindView(R.id.followerAmountProfile)
    TextView textViewFollowerAmountProfile;

    @BindView(R.id.veganPointsProfile)
    TextView textViewVeganPointsProfile;

    @BindView(R.id.linearLayoutProfile)
    LinearLayout linearLayoutProfile;

    @BindView(R.id.challengeRecyclerView)
    RecyclerView challengeRecyclerView;

    private static final int PICK_IMAGE = 0;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private UploadImageTask uploadImageTask;
    private Map response;


    private File file;
    private String uri;
    private String imageUrl;
    private ArrayList<Challenge> challenges = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ProfileChallengeAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
            callApi();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);


        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProfileChallengeAdapter(challenges);

        challengeRecyclerView.setLayoutManager(layoutManager);
        challengeRecyclerView.setAdapter(adapter);
        challengeRecyclerView.setHasFixedSize(true);


        return rootView;

    }

    @OnClick(R.id.linearLayoutProfile)
    public void onClick() {
        FollowerFragment fragment = new FollowerFragment();
        Bundle extras = new Bundle();
        extras.putString("user", new Gson().toJson(user));
        fragment.setArguments(extras);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.fab)
    public void onClickFab(){
        pickImage();

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
            Picasso.with(getActivity()).load(file).fit().into(imageViewUserProfile);
            uploadImage();
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


    private void callApi() {
        Call<User> userCall = service.getUserById(token.getUserid());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    updateView(user);
                    getUserChallenges();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void postUser(){
        Call<User> userCall = service.updateUser(user.get_id(), user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    user.setToken(token);
                    updateView(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void getUserChallenges(){
        Call<List<Challenge>> challengeCall = service.getChallengesUser(user.get_id());
        challengeCall.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if(response.isSuccessful()){
                    challenges.addAll(response.body());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {

            }
        });
    }

    private void updateView(User user) {
        if(user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(imageViewUserProfile.getContext()).load(user.getImage()).fit().into(imageViewUserProfile);
        textViewUsernameProfile.setText(user.getFullName());
        textViewFollowerAmountProfile.setText(String.valueOf(user.getFollowingUsers().length) + " followers");
        textViewVeganPointsProfile.setText(String.valueOf(user.getTotalVeganScore()) + " vegan score");
        MainActivity activity = (MainActivity)getActivity();
        activity.updateView(user);


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
                user.setImage(imageUrl);
                postUser();
            } else {


            }
        }

        @Override
        protected void onCancelled() {
            uploadImageTask = null;
        }


    }
}
