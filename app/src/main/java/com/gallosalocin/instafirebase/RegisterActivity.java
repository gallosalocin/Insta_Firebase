package com.gallosalocin.instafirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gallosalocin.instafirebase.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        binding.loginUserTxt.setOnClickListener(view1 -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        binding.registerBtn.setOnClickListener(view1 -> {
            String txtUsername = binding.username.getText().toString();
            String txtName = binding.name.getText().toString();
            String txtEmail = binding.email.getText().toString();
            String txtPassword = binding.password.getText().toString();

            if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6) {
                Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtUsername, txtName, txtEmail, txtPassword);
            }
        });
    }

    private void registerUser(String username, String name, String email, String password) {
        pd.setMessage("Please wait");
        pd.show();

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("name", name);
            map.put("email", email);
            map.put("id", auth.getCurrentUser().getUid());
            map.put("bio", "");
            map.put("imageUrl","default");

            dbRef.child("users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(this, "Update the profile for better experience", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
