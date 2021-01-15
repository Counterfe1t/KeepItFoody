package com.example.kuba.keepitfoody;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.regex.Pattern;

/**
 * Activity used to create user account.
 */
public class RegistrationActivity extends AppCompatActivity {

    //Variables required to register
    private TextInputLayout Name;
    private TextInputLayout Email;
    private TextInputLayout Password;
    private TextInputLayout Password2;

    //Password pattern
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            //"(?=.*[a-zA-Z])" +      //any letter
            //"(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{5,}" +               //at least 5 characters
            "$");

    //Email pattern
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +    //looking for charrs dot and next charrs
                    "\\@" +                                     // looking for @
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +         //looking for charrs
                    "(" +
                    "\\." +                                 //looking for dot
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +     //looking for charrs
                    ")+"
    );


    //Validation
    private TextView Info;
    private Button registrationButton;
    private CheckBox Regulations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Rejestracja");

        Name = findViewById(R.id.etName_1);
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword_1);
        Password2 = findViewById(R.id.etPassword2);
        Regulations = findViewById(R.id.btn_regulations);

        Info = findViewById(R.id.textView_Info);
        registrationButton = findViewById(R.id.btn_registration);

        //Checking if fields are not empty
        Name.getEditText().addTextChangedListener(registrationTextWatcher);
        Email.getEditText().addTextChangedListener(registrationTextWatcher);
        Password.getEditText().addTextChangedListener(registrationTextWatcher);
        Password2.getEditText().addTextChangedListener(registrationTextWatcher);

        TextView textViewRegulationHyperlink = findViewById(R.id.textViewRegulationHyperlink);
        textViewRegulationHyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://192.168.134.62/assets/regulamin.pdf"));
                startActivity(intent);
            }
        });

        Button buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(v -> {
            openSignInActivity();
            finish();
        });
    }

    /**
     * Validate text fields and send registration request to server.
     * @param view
     */
    public void sendRegistration(View view){
        String nick = Name.getEditText().getText().toString();
        String email = Email.getEditText().getText().toString();
        String pass = Password.getEditText().getText().toString();
        String pass2 = Password2.getEditText().getText().toString();

        //Validation before send
        boolean flag = true;

        //Initatning validation functions
        if (!validateName() | !validateEmail() | !validatePassword() | !validateRegulations()){
            flag = false;
        }

        //Connecting to restapi
        if(flag) {

            SignUpBackground signUpBackground = new SignUpBackground(this, nick, email, pass, pass2);

            try {
                signUpBackground.sendRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //Checking if fields are empty
    private TextWatcher registrationTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String loginInput = Name.getEditText().getText().toString().trim();
            String emailInput = Email.getEditText().getText().toString().trim();
            String passwordInput = Password.getEditText().getText().toString().trim();
            String passwordInput2 = Password2.getEditText().getText().toString().trim();

            registrationButton.setEnabled(!loginInput.isEmpty() && !emailInput.isEmpty() && !passwordInput.isEmpty() && !passwordInput2.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validateEmail(){
        String emailInput = Email.getEditText().getText().toString().trim();

        if(!EMAIL_ADDRESS.matcher(emailInput).matches()){
            Email.setError("Niepoprawny adres email");
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String passwordInput = Password.getEditText().getText().toString().trim();

        if (!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            Password.setError("Hasło powinno mieć minimum 5 znaków, dużą i małą literę oraz cyfrę");
            return false;
        } else {
            Password.setError(null);
            return true;
        }
    }

    private boolean validateName(){
        String nameInput = Name.getEditText().getText().toString().trim();

        if(nameInput.length() > 50){
            Name.setError("Imię nie może mieć więcej niż 50 znaków");
            return false;
        } else {
            Name.setError(null);
            return true;
        }
    }

    private boolean validateRegulations(){
        if(!Regulations.isChecked()){
            Info.setText("Zaakceptuj regulamin");
            Info.setVisibility(View.VISIBLE);
            return false;
        } else {
            Info.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private void openSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
    }

}
