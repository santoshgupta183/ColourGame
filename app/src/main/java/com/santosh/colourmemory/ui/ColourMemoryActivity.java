package com.santosh.colourmemory.ui;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.santosh.colourmemory.R;
import com.santosh.colourmemory.databinding.ActivityColourMemoryBinding;

public class ColourMemoryActivity extends AppCompatActivity {

    public ActivityColourMemoryBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityColourMemoryBinding.inflate(getLayoutInflater());

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController= navHostFragment.getNavController();

        binding.toolbar.setTitle("");
        binding.tvScore.setText("0");
        setSupportActionBar(binding.toolbar);

        setupActionBarWithNavController(this, navController);
        setContentView(binding.getRoot());

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}