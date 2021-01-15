package com.example.kuba.keepitfoody;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Activity used to handle login attempts.
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    TextInputEditText editEmail;
    TextInputEditText editPassword;
    Context context;

    //Google
    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_GOOGLE = 9001;

    //Facebook
    CallbackManager callbackManager;
    ProgressDialog mDialog;
    String email;
    String birthday;
    String firstName;
    ImageView imgAvatar;
    String profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Logowanie");

        context = this;

        Intent intent = getIntent();
        String rememberedEmail = intent.getStringExtra("email");
        String rememberedPassword = intent.getStringExtra("password");

        if (rememberedEmail != null) {
            try {
                SignInBackground signInBackground = new SignInBackground(context, rememberedEmail, rememberedPassword);
                signInBackground.sendRequest();
            } catch (JSONException e) {
                Log.e("ERROR", "LOGIN_ACTIVITY", e);
            }
        }

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        Button buttonSignIn = findViewById(R.id.buttonLogin);
        buttonSignIn.setOnClickListener(v -> {
            String login = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Nie wprowadzono danych logowania!", Toast.LENGTH_SHORT).show();
            } else if (login.equals("dev") && password.equals("dev")) {
                try {
                    SignInBackground signInBackground = new SignInBackground(context, "none@smart-mail.top", "!Q@W3e4r");
                    signInBackground.sendRequest();
                } catch (JSONException e) {
                    Log.e("ERROR", "LOGIN_ACTIVITY", e);
                }
            } else {
                try {
                    SignInBackground signInBackground = new SignInBackground(context, login, password);
                    signInBackground.sendRequest();
                } catch (JSONException e) {
                    Log.e("ERROR", "LOGIN_ACTIVITY", e);
                }
            }
        });

        // Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButtonFacebook = findViewById(R.id.buttonLoginFacebook);
        loginButtonFacebook.setReadPermissions(Arrays.asList("email"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                String accesstoken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                    mDialog.dismiss();
                    Log.e("ERR", response.toString());
                    getFacebookData(object);
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                //parameters.putString("fields","first_name,id,email,birthday");
                parameters.putString("fields","email");
                request.setParameters(parameters);
                request.executeAsync();

                //Executing asynctask for Api Facebook
                //FacebookLoginBackground fb = new FacebookLoginBackground(LoginActivity.this);
                //fb.execute(email, firstName, birthday);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("ERR", error.toString(), null);
            }
        });

        //If already login
        //if(AccessToken.getCurrentAccessToken() != null){}


        //Google
        SignIn = findViewById(R.id.google_login);
        SignIn.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


        // Przycisk do rejestracji
        Button buttonSignUp = findViewById(R.id.buttonRegister);
        buttonSignUp.setOnClickListener(v -> {
            Intent intent1 = new Intent(context, RegistrationActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        //Google
        if(requestCode == REQUEST_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);

        }
    }

    private void getFacebookData(JSONObject object) {
        try{

            //firstName = object.getString("first_name");
            email = object.getString("email");
            //birthday = object.getString("birthday");

            //URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
            //loadImageFromUrl(profilePicture);

            /*
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeStream(profile_picture.openConnection().getInputStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            profilePicture = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

            //name.setText(object.getString("name"));
            //email.setText(object.getString("email"));
            //birthday.setText(object.getString("birthday"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_login:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Google signin
    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQUEST_GOOGLE);

    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String img_url = account.getPhotoUrl().toString();
        }
    }

    //Loading picture from URL
    private void loadImageFromUrl(String url){
        Picasso.with(getParent()).load(url).into(imgAvatar, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError() {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
