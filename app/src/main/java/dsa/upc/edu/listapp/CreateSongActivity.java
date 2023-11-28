package dsa.upc.edu.listapp;

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

public class CreateSongActivity extends AppCompatActivity {

    EditText editTextSongName;
    EditText editTextSinger;
    Button createSongBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_song);

        // Initialize the text views and buttons here
        editTextSinger = findViewById(R.id.editTextSinger);
        editTextSongName = findViewById(R.id.editTextSongName);

        createSongBtn = findViewById(R.id.createSongBtn);
        createSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSong(view);
            }
        });
    }

    private String generateRandomString() {
        // Generate a random UUID and extract its string representation
        return UUID.randomUUID().toString();
    }

    public void createSong(View view) {
        TracksService tracksService = TracksService.retrofit.create(TracksService.class);//creating interface
        String songName = editTextSongName.getText().toString();
        String singer = editTextSinger.getText().toString();
        String randomId = generateRandomString();

        Song newSong = new Song(randomId, songName, singer);

        Call<Song> callCreateSong = tracksService.createSong(newSong);

        callCreateSong.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateSongActivity.this, "Song Created Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the current activity if song is created successfully
                } else {
                    System.out.println("Error: " + response.code() + " " + response.message());
                    Toast.makeText(CreateSongActivity.this, "Error" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(CreateSongActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
