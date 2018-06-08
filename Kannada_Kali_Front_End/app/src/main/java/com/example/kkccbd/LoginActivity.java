package com.example.kkccbd;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity  extends AppCompatActivity {

    private final String username = "avinash10207@gmail.com";
    private final String password = "123456789";
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else if (email.equals(username)) {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        } else {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        }


    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else if (inputPassword.getText().toString().equals(password)){
            inputLayoutPassword.setErrorEnabled(false);
            return true;
        }else {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputPassword);
            return false;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

        }
    }

    private void login() {
        if(validateEmail() && validatePassword()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String message = "Login";
            intent.putExtra(Util.EXTRA_MESSAGE, message);
            startActivity(intent);
        } else {
            if(!validateEmail()) {
                inputLayoutEmail.setError(getString(R.string.err_msg_email));
                requestFocus(inputEmail);
            } else {
                inputLayoutPassword.setError(getString(R.string.err_msg_password));
                requestFocus(inputPassword);
            }
        }
    }


}




