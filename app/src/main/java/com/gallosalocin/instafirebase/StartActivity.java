package com.gallosalocin.instafirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.gallosalocin.instafirebase.databinding.ActivityStartBinding;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.linearLayout.animate().alpha(0f).setDuration(10);
        iconAnimation();

        binding.registerBtn.setOnClickListener(view1 -> {
            startActivity(new Intent(StartActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        binding.loginBtn.setOnClickListener(view1 -> {
            startActivity(new Intent(StartActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

    }

    // ICON ANIMATION

    private void iconAnimation(){
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1500);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        binding.icon.setAnimation(animation);
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            binding.icon.clearAnimation();
            binding.icon.setVisibility(View.INVISIBLE);
            binding.linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(StartActivity.this, MainActivity.class));
//            finish();
//        }
//    }
}
