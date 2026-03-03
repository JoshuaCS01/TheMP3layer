package edu.uga.cs.themp3layer;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FragmentManager fm;

    private boolean firstOpen = false;
    private boolean songPlaying = false;
    private boolean songSelected = false;

    private View songProfile;
    public PlaybackManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            fm = getSupportFragmentManager();

            bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnItemSelectedListener(item -> {
                switchFragment(item.getItemId());
                return true;
            });


            if (firstOpen == false) {
                bottomNav.setSelectedItemId(R.id.nav_songs); // change to whichever id should be default
                firstOpen = true;
            }
            return insets;
        });

        songProfile = findViewById(R.id.include);
        songProfile.setVisibility(View.INVISIBLE);

        //Prevents songs behind banner from being selected
        View banner = findViewById(R.id.include);
        banner.setOnClickListener(v -> {
        });

        manager = PlaybackManager.getInstance(this);
        manager.setListener(() -> {
            showSongProfile();
        });


        //Play/Pause button.
        ImageButton pause = findViewById(R.id.playButton);
        pause.setOnClickListener(v -> {
        manager = PlaybackManager.getInstance(this);
            if (manager.getIsPlaying()) {
                manager.pause();
                pause.setImageResource(R.drawable.baseline_play_arrow_24);
            } else {
                manager.resume();
                pause.setImageResource(R.drawable.baseline_pause_24);
            }
        });

        //next
        ImageButton next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            SongsFragment fragment = (SongsFragment) getSupportFragmentManager().findFragmentByTag("Songs");
            fragment.skip();
        });

        //Previous
        ImageButton prev = findViewById(R.id.prevButton);
        prev.setOnClickListener(v -> {
            SongsFragment fragment = (SongsFragment) getSupportFragmentManager().findFragmentByTag("Songs");
            fragment.prev();
        });

    }


    public void showSongProfile() {
        manager = PlaybackManager.getInstance(this);
        Song current = manager.getCurrentSong();

        if (current != null) {
            songProfile.setVisibility(View.VISIBLE);
            TextView title = findViewById(R.id.songNamePlaying);
            TextView artist = findViewById(R.id.songArtistPlaying);
            ImageButton pause = findViewById(R.id.playButton);
            pause.setImageResource(R.drawable.baseline_pause_24);

            title.setSelected(true);
            title.setText(current.getName());
            artist.setText(current.getArtist());

        }
    }




    private void switchFragment(int itemId) {
        Fragment current = fm.findFragmentById(R.id.fragment_container);
        Fragment to = null;
        String tag = null;

        if (itemId == R.id.nav_home) {
            tag = "Home";
            to = fm.findFragmentByTag(tag);
            if (to == null) to = new HomeFragment(); // implement later
        } else if (itemId == R.id.nav_songs) {
            tag = "Songs";
            to = fm.findFragmentByTag(tag);
            if (to == null) to = new SongsFragment(); // implement later
        } else if (itemId == R.id.nav_playlists) {
            tag = "Playlists";
            to = fm.findFragmentByTag(tag);
            if (to == null) to = new PlaylistsFragment(); // implement later
        }

        if (to != null && to != current) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, to, tag)
                    .commit();
        }
    }
}