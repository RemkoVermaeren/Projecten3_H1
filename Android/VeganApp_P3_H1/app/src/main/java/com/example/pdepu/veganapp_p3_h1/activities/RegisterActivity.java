package com.example.pdepu.veganapp_p3_h1.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pdepu.veganapp_p3_h1.R;
import com.example.pdepu.veganapp_p3_h1.models.Token;
import com.example.pdepu.veganapp_p3_h1.models.User;
import com.example.pdepu.veganapp_p3_h1.network.Service;
import com.example.pdepu.veganapp_p3_h1.network.ServicesInitializer;
import com.google.gson.Gson;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by pdepu on 2/08/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText editTextName;
    @BindView(R.id.surname)
    EditText editTextSurname;
    @BindView(R.id.password)
    EditText editTextPassword;
    @BindView(R.id.email)
    EditText editTextEmail;
    @BindView(R.id.already_registered)
    TextView textViewAlreadyRegistered;

    @BindView(R.id.email_register_button)
    Button email_register_button;

    @BindView(R.id.register_form)
    View viewRegisterForm;
    @BindView(R.id.register_progress)
    View viewRegisterProgress;


    private String message;
    private UserRegisterTask registerTask = null;
    private Service service = new ServicesInitializer().initializeService();
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.already_registered)
    public void onClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.email_register_button)
    public void OnClickRegister() {
        attemptRegister();
    }

    private void attemptRegister() {


        if (registerTask != null) {
            return;
        }

        // Reset errors.
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextName.setError(null);
        editTextSurname.setError(null);

        // Store values at the time of the login attempt.
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(name)) {
            editTextName.setError(getString(R.string.error_field_required));
            focusView = editTextName;
            cancel = true;
        } else if (TextUtils.isEmpty(surname)) {
            editTextSurname.setError(getString(R.string.error_field_required));
            focusView = editTextSurname;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UserRegisterTask(name, surname, email, password);
            registerTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            viewRegisterForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            viewRegisterProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            viewRegisterProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewRegisterProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            viewRegisterProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            viewRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mSurname;

        UserRegisterTask(String name, String surname, String email, String password) {
            mEmail = email;
            mPassword = password;
            mName = name;
            mSurname = surname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            User user = new User(mEmail, mName, mSurname, Calendar.getInstance().getTime(), false, mPassword);
            try {
                Response<Token> response = service.registerUser(user).execute();
                if (response.errorBody() != null)
                    message = response.errorBody().string();
                if(response.body() != null){
                    token = response.body();
                }
                return response.isSuccessful();
            } catch (Exception e) {
                return false;
            }

        }


        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String tokenString = gson.toJson(token);
                bundle.putString("tokenString",tokenString);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                if (message != null && !message.isEmpty() && message.toLowerCase().contains("already") && message.toLowerCase().contains("user")) {
                    editTextName.setError(getString(R.string.userExists));
                    editTextName.requestFocus();
                } else if (message != null && !message.isEmpty()) {
                    editTextName.setError(getString(R.string.error_network));
                    editTextName.requestFocus();
                }
                message = "";

            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }


    }
}
