package com.example.noteapp.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.noteapp.R;
import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.example.noteapp.models.Note;
import com.example.noteapp.utils.Constants;
import com.example.noteapp.viewmodel.CreateNoteViewModel;

public class CreateNoteActivity extends AppCompatActivity {

    private ActivityCreateNoteBinding binding;
    private CreateNoteViewModel createNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUI();
    }

    private void actions() {
        binding.imageBack.setOnClickListener( v -> onBackPressed());
        binding.imageSave.setOnClickListener( v -> saveNote());
    }

    private void setUI() {
        createNoteViewModel = new ViewModelProvider(this).get(CreateNoteViewModel.class);
        binding.textDateTime.setText(Constants.Companion.getCurrentDate());
        actions();

        createNoteViewModel.isLoading().observe(this, aBoolean -> {
           if (aBoolean) {
               View view = this.getCurrentFocus();
               if (view != null) {
                   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
               }
               Intent intent = new Intent(this, MainActivity.class);
               setResult(RESULT_OK, intent);
               finish();
           }
        });
    }

    private void saveNote() {
        if (Constants.Companion.isNullEditText(binding.inputNoteTitle)) {
            Constants.Companion.showToastS(this, getString(R.string.empty_title_error));
            return;
        } else if (Constants.Companion.isNullEditText(binding.inputNoteSubtitle) &&
                Constants.Companion.isNullEditText(binding.inputNoteText)
        ) {
            Constants.Companion.showToastS(this, getString(R.string.note_text_empty_error));
            return;
        }

        Note note = new Note();
        note.setTitle(String.valueOf(binding.inputNoteTitle.getText()));
        note.setSubTitle(String.valueOf(binding.inputNoteSubtitle.getText()));
        note.setNoteText(String.valueOf(binding.inputNoteText.getText()));
        note.setDateTime(String.valueOf(binding.textDateTime.getText()));

        createNoteViewModel.saveNote(note);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}