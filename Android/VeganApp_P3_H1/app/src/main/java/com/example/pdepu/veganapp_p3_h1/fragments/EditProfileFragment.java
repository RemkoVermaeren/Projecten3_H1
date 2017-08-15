package com.example.pdepu.veganapp_p3_h1.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.activities.MainActivity;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.example.pdepu.veganapp_p3_h1.views.UriHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
 * Created by pdepu on 13/08/2017.
 */

public class EditProfileFragment extends Fragment {

    private Service service;
    private Token token;
    private User user;

    @BindView(R.id.addPictureLayout)
    RelativeLayout addPictureLayout;

    @BindView(R.id.imageViewUserEdit)
    ImageView imageViewUserEdit;

    @BindView(R.id.nameEdit)
    TextView textViewNameEdit;

    @BindView(R.id.surnameEdit)
    TextView textViewSurnameEdit;

    @BindView(R.id.emailEdit)
    TextView textViewEmailEdit;


    private static final int PICK_IMAGE = 0;


    private UploadImageTask uploadImageTask;
    private Map response;


    private File file;
    private String uri;
    private String imageUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        service = new ServicesInitializer().initializeService();
        if (getArguments() != null) {
            token = new Gson().fromJson(getArguments().getString("tokenString"), Token.class);
            callApi();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            checkForEmptyFields();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkForEmptyFields() {
        textViewEmailEdit.setError(null);
        textViewSurnameEdit.setError(null);
        textViewNameEdit.setError(null);


        String email = textViewEmailEdit.getText().toString();
        String name = textViewNameEdit.getText().toString();
        String surname = textViewSurnameEdit.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(name)) {
            textViewNameEdit.setError(getString(R.string.error_field_required));
            focusView = textViewNameEdit;
            cancel = true;
        } else if (TextUtils.isEmpty(surname)) {
            textViewSurnameEdit.setError(getString(R.string.error_field_required));
            focusView = textViewSurnameEdit;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            textViewEmailEdit.setError(getString(R.string.error_field_required));
            focusView = textViewEmailEdit;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textViewEmailEdit.setError(getString(R.string.error_invalid_email));
            focusView = textViewEmailEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            user.setName(name);
            user.setSurName(surname);
            user.setFullName(name + " " + surname);
            user.setUsername(email);
            if (file != null)
                uploadImage();
            else
                postUser();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    @OnClick(R.id.addPictureLayout)
    public void onClick() {
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
            String path = UriHandler.getPath(getContext(), data.getData());
            file = new File(path);
            uri = path;
            Picasso.with(getActivity()).load(file).resize(120, 120).into(imageViewUserEdit);
        }
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
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void postUser() {
        Call<User> userCall = service.updateUser(user.get_id(), user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    startProfileFragment();
                    Log.i("Success", "Save succeeded");
                } else if (response.errorBody() != null && !response.errorBody().toString().isEmpty()) {
                    JSONObject message = null;
                    try {
                        message = new JSONObject(response.errorBody().string());
                        if (message.getString("message").contains("already")) {
                            textViewEmailEdit.setError(getString(R.string.userExists));
                            textViewEmailEdit.requestFocus();
                        } else {
                            textViewNameEdit.setError(getString(R.string.error_network));
                            textViewNameEdit.requestFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("failure", t.toString());
            }
        });

    }

    private void startProfileFragment() {
        ProfileTabFragment fragment = new ProfileTabFragment();
        Bundle extras = new Bundle();
        extras.putString("tokenString", new Gson().toJson(token));
        fragment.setArguments(extras);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void updateView(User user) {
        if (user.getImage() != null && !user.getImage().isEmpty())
            Picasso.with(imageViewUserEdit.getContext()).load(user.getImage()).fit().into(imageViewUserEdit);
        textViewNameEdit.setText(user.getName());
        textViewSurnameEdit.setText(user.getSurName());
        textViewEmailEdit.setText(user.getUsername());
        MainActivity activity = (MainActivity) getActivity();
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
                if (response != null) {
                    imageUrl = response.get("url").toString();
                    return true;
                } else
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
                View focusView = textViewNameEdit;
                textViewNameEdit.setError(getString(R.string.error_network));
                focusView.requestFocus();

            }
        }

        @Override
        protected void onCancelled() {
            uploadImageTask = null;
        }


    }
}

