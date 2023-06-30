package com.example.noteapp.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteapp.adapter.NoteAdapter;
import com.example.noteapp.databinding.ActivityMainBinding;
import com.example.noteapp.models.Note;
import com.example.noteapp.utils.Constants;
import com.example.noteapp.utils.RequestReturnCodes;
import com.example.noteapp.viewmodel.CreateNoteViewModel;
import com.example.noteapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel mainActivityViewModel;
    private NoteAdapter adapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList);
        binding.notesRecyclerView.setAdapter(adapter);
        fetchData();
        actions();
    }

    private void fetchData() {
        noteList.clear();
        mainActivityViewModel.fetchNotes();
    }

    private void actions(){
        observers();
        binding.imageAddNoteMain.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            startActivityForResultAddNote.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> startActivityForResultAddNote = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    fetchData();
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestReturnCodes.REQUEST_CODE_ADD_NOTE){
        }
    }

    private void observers(){
        mainActivityViewModel.getNotes().observe(this, notes -> {
            if (noteList.size() == 0){
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
            } else {
                noteList.add(0, notes.get(0));
                adapter.notifyItemInserted(0);
            }
            binding.notesRecyclerView.smoothScrollToPosition(0);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}