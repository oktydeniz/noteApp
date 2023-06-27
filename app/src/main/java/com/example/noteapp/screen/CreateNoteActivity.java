package com.example.noteapp.screen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.noteapp.R;
import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.example.noteapp.models.Note;

public class CreateNoteActivity extends AppCompatActivity {

    private ActivityCreateNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actions();
    }

    private void actions() {
        binding.imageBack.setOnClickListener( v -> onBackPressed());
    }
}