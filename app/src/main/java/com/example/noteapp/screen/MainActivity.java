package com.example.noteapp.screen;

import static com.example.noteapp.utils.RequestReturnCodes.REQUEST_CODES_SHOW_NOTE;
import static com.example.noteapp.utils.RequestReturnCodes.REQUEST_CODES_UPDATE_NOTE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteapp.adapter.NoteAdapter;
import com.example.noteapp.databinding.ActivityMainBinding;
import com.example.noteapp.listener.NoteListener;
import com.example.noteapp.models.Note;
import com.example.noteapp.utils.Constants;
import com.example.noteapp.utils.RequestReturnCodes;
import com.example.noteapp.viewmodel.CreateNoteViewModel;
import com.example.noteapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListener {

    private ActivityMainBinding binding;
    private MainActivityViewModel mainActivityViewModel;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList, this);
        binding.notesRecyclerView.setAdapter(adapter);
        fetchData();
        actions();
    }

    private void fetchData() {
        noteList.clear();
        binding.notesRecyclerView.removeAllViews();
        mainActivityViewModel.fetchNotes();
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    private void actions(){
        observers(RequestReturnCodes.REQUEST_CODES_SHOW_NOTE);
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


    ActivityResultLauncher<Intent> updateNoteActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    observers(RequestReturnCodes.REQUEST_CODES_UPDATE_NOTE);
                }
            }
    );

    private void observers(int request){
        mainActivityViewModel.getNotes().observe(this, notes -> {
            noteList = null;
            if (request == RequestReturnCodes.REQUEST_CODE_ADD_NOTE) {
                noteList.add(0, notes.get(0));
                adapter.notifyItemInserted(0);
                binding.notesRecyclerView.smoothScrollToPosition(0);
            } else if(request == REQUEST_CODES_SHOW_NOTE) {
                noteList.addAll(notes);
                adapter.notifyDataSetChanged();
            } else if (request == REQUEST_CODES_UPDATE_NOTE){
                noteList.remove(noteClickedPosition);
                noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                adapter.notifyItemChanged(noteClickedPosition);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onNoteClicked(@NonNull Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        updateNoteActivityResult.launch(intent);

    }
}