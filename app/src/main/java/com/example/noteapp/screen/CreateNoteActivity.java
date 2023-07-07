package com.example.noteapp.screen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.noteapp.R;
import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.example.noteapp.models.Note;
import com.example.noteapp.utils.Constants;
import com.example.noteapp.utils.RequestReturnCodes;
import com.example.noteapp.viewmodel.CreateNoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.regex.Pattern;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCreateNoteBinding binding;
    private CreateNoteViewModel createNoteViewModel;
    private String selectedColor = "#333333";
    private String selectedNoteImgPath = "";

    private AlertDialog alertDialog;

    private Note alreadyAvailableNote = null;

    ImageView imageColor1;
    ImageView imageColor2;
    ImageView imageColor3;
    ImageView imageColor4;
    ImageView imageColor5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUI();
        initMiscellaneous();
        setSubTitleIndicatorColor();

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
            initMiscellaneous();
        }
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

    private void setViewOrUpdateNote() {
        binding.inputNoteText.setText(alreadyAvailableNote.getNoteText());
        binding.inputNoteSubtitle.setText(alreadyAvailableNote.getSubTitle());
        binding.inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        binding.textDateTime.setText(alreadyAvailableNote.getDateTime());

        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            binding.imageNote.setVisibility(View.VISIBLE);
            selectedNoteImgPath = alreadyAvailableNote.getImagePath();
        }

        if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()){
            binding.textWebUrl.setText(alreadyAvailableNote.getWebLink());
            binding.layoutWebUrl.setVisibility(View.VISIBLE);

        }
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
        note.setColor(selectedColor);
        note.setImagePath(selectedNoteImgPath);

        if (binding.layoutWebUrl.getVisibility() == View.VISIBLE){
            note.setWebLink(binding.textWebUrl.getText().toString());
        }
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }
        createNoteViewModel.saveNote(note);
    }

    private void initMiscellaneous() {
       final LinearLayout linearLayout = findViewById(R.id.layoutMiscellaneous);
       final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
       linearLayout.findViewById(R.id.textMiscellaneous).setOnClickListener( v -> {
           if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
               bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
           } else {
               bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
           }
       });

         imageColor1 = linearLayout.findViewById(R.id.imageColor1);
         imageColor2 = linearLayout.findViewById(R.id.imageColor2);
         imageColor3 = linearLayout.findViewById(R.id.imageColor3);
         imageColor4 = linearLayout.findViewById(R.id.imageColor4);
         imageColor5 = linearLayout.findViewById(R.id.imageColor5);

       linearLayout.findViewById(R.id.viewColor1).setOnClickListener(this);
       linearLayout.findViewById(R.id.viewColor2).setOnClickListener(this);
       linearLayout.findViewById(R.id.viewColor3).setOnClickListener(this);
       linearLayout.findViewById(R.id.viewColor4).setOnClickListener(this);
       linearLayout.findViewById(R.id.viewColor5).setOnClickListener(this);

       linearLayout.findViewById(R.id.layoutAddImage).setOnClickListener( v -> {
           bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
           if (Build.VERSION.SDK_INT >= 33) {
               if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                       != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, RequestReturnCodes.REQUEST_CODES_STORAGE_PERMISSION);
               } else {
                   selectImage();
               }
           } else {
               if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RequestReturnCodes.REQUEST_CODES_STORAGE_PERMISSION);
               } else {
                   selectImage();
               }
           }

       });
       if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null &&
       !alreadyAvailableNote.getColor().trim().isEmpty()){
           switch (alreadyAvailableNote.getColor()){
               case "#FDBE3B": linearLayout.findViewById(R.id.viewColor2).performClick();break;
               case "#FF4842": linearLayout.findViewById(R.id.viewColor3).performClick();break;
               case "#3A52FC": linearLayout.findViewById(R.id.viewColor4).performClick();break;
               case "#000000": linearLayout.findViewById(R.id.viewColor5).performClick();break;
           }
       }

       linearLayout.findViewById(R.id.layoutAddUrl).setOnClickListener( v -> {
           bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
           showAddUrlDialog();
       });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResultGetImg.launch(intent);
        }
    }


    ActivityResultLauncher<Intent> startActivityForResultGetImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null) {
                    Bitmap bitmap = Constants.Companion.getImgFromURI(result.getData(), CreateNoteActivity.this);
                    if (bitmap != null) {
                        binding.imageNote.setVisibility(View.VISIBLE);
                        binding.imageNote.setImageBitmap(bitmap);

                        selectedNoteImgPath = Constants.Companion.getFileFromURI(CreateNoteActivity.this, intent.getData());
                    }
                }
            }
    );

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewColor1:
                selectedColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicatorColor();
                break;
            case R.id.viewColor2:
                selectedColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicatorColor();
                break;
            case R.id.viewColor3:
                selectedColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubTitleIndicatorColor();
                break;
            case R.id.viewColor4:
                selectedColor = "#3A52FC";
                imageColor1.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setSubTitleIndicatorColor();
                break;
            case R.id.viewColor5:
                selectedColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setSubTitleIndicatorColor();
                break;
            default:
                break;
        }
    }

    private void setSubTitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) findViewById(R.id.viewSubtitleIndicator).getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));
    }

    private void showAddUrlDialog() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);

            alertDialog = builder.create();

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputUrl = view.findViewById(R.id.inputURL);
            inputUrl.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener( v -> {
                if (inputUrl.getText().toString().trim().isEmpty()){
                    Constants.Companion.showToastS(CreateNoteActivity.this, getString(R.string.enter_url_error));
                } else if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString()).matches()){
                    Constants.Companion.showToastL(CreateNoteActivity.this, getString(R.string.enter_valid_url_error));
                } else {
                    binding.textWebUrl.setText(inputUrl.getText().toString());
                    binding.layoutWebUrl.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(v -> alertDialog.dismiss());
        }

        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}