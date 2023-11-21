package dsa.upc.edu.listapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dsa.upc.edu.listapp.tracks.Song;
import dsa.upc.edu.listapp.tracks.TracksService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Based on http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
//      and https://zeroturnaround.com/rebellabs/getting-started-with-retrofit-2/

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = findViewById(R.id.my_swipe_refresh);

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the adapter
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        doApiCall(null);

        // Manage swipe on items
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false; // Return false since you're not supporting item reordering
                    }

                    //Aqui agregamos funcion api para eliminar una cancion
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        // Check if the adapter is not null
                        if (adapter != null) {
                            // Get the ID directly using the adapter position
                            int position = viewHolder.getAdapterPosition();
                            Song song = adapter.getItem(position);

                            // Check if the song is not null
                            if (song != null) {
                                String id = song.id;

                                // Remove the item from the adapter
                                adapter.remove(position);

                                // Perform your delete operation using the obtained ID
                                TracksService tracksService = TracksService.retrofit.create(TracksService.class);
                                Call<Void> callDeleteSong = tracksService.deleteSong(id);
                                callDeleteSong.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Song has been deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }


                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doApiCall(swipeRefreshLayout);
                    }
                }
        );

    }

    private void doApiCall(final SwipeRefreshLayout mySwipeRefreshLayout) {
        TracksService tracksService = TracksService.retrofit.create(TracksService.class);//creating interface
        Call<List<Song>> call = tracksService.songs();

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                // set the results to the adapter
                adapter.setData(response.body());// we have to modify our adapter to handle this

                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);

                String msg = "Error in retrofit: "+t.toString();
                Log.d(TAG,msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
