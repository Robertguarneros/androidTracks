package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import dsa.upc.edu.listapp.tracks.Song;
import dsa.upc.edu.listapp.tracks.TracksService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSongActivity extends AppCompatActivity {

    EditText editTextSongName;
    EditText editTextSinger;
    Button createSongBtn;
    String songID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_song);

        // Initialize the text views and buttons here
        editTextSinger = findViewById(R.id.editTextSinger);
        editTextSongName = findViewById(R.id.editTextSongName);

        createSongBtn = findViewById(R.id.createSongBtn);
        Intent intent = getIntent();
        songID = intent.getStringExtra("ID");
        createSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSong(view);
            }
        });
    }

    private String generateRandomString() {
        // Generate a random UUID and extract its string representation
        return UUID.randomUUID().toString();
    }

    public void editSong(View view) {
        TracksService tracksService = TracksService.retrofit.create(TracksService.class);//creating interface
        String songName = editTextSongName.getText().toString();
        String singer = editTextSinger.getText().toString();

        Song newSong = new Song(songID, songName, singer);

        Call<Void> callUpdateSong = tracksService.updateSong(newSong);

        callUpdateSong.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditSongActivity.this, "Song Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the current activity if song is edited successfully
                } else {
                    System.out.println("Error: " + response.code() + " " + response.message());
                    Toast.makeText(EditSongActivity.this, "Error" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(EditSongActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
