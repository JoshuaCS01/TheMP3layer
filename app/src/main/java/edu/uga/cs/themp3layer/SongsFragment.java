package edu.uga.cs.themp3layer;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    Boolean songsPopulated = false;

    Boolean hasPermission = false;

    Button button;
    SearchView searchBar;
    RecyclerView songList;

    ArrayList<Song> audioLists = new ArrayList<>();

    PlaybackManager playbackManager;

    String searchFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_songs, container, false);
        button = root.findViewById(R.id.button);
        searchBar = root.findViewById(R.id.searchView);
        songList = root.findViewById(R.id.songList);


        hasPermission = ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        updateUIElements(hasPermission);


        MediaStore mStore= new MediaStore();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("tagUIt", String.valueOf(MediaStore.canManageMedia(requireContext())));
        }

        String[] strings = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.GENRE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.IS_MUSIC};// Can include more data for more details and check it.


        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strings, null, null, null);
        int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        int nameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
        int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int genreIndex = cursor.getColumnIndex(MediaStore.Audio.Media.GENRE);
        int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(idIndex));

                    Song toBeAdded = new Song(cursor.getLong(idIndex),
                                              cursor.getString(nameIndex),
                                              cursor.getString(artistIndex),
                                              cursor.getString(genreIndex),
                                              cursor.getString(albumIndex),
                                              null, contentUri);

                    if(isMusic != 0) {
                        audioLists.add(toBeAdded);
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        SListAdapter listAdapter = new SListAdapter(audioLists, requireContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        songList.setLayoutManager(linearLayoutManager);
        songList.setAdapter(listAdapter);

        playbackManager = PlaybackManager.getInstance(requireContext());


        audioLists.removeIf(p -> p.getName().contains("ogg"));
        playbackManager.setAudioList(audioLists);

        Log.d("count", "Number of Songs: " + audioLists.size());

        listAdapter.setOnItemClickListener(song -> {
            playbackManager.play(requireContext(), song);

            ((MainActivity) requireActivity()).showSongProfile();
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPermissions();
                updateUIElements(hasPermission);

            }
        });

        return root;
    }

    public void skip() {
        playbackManager.skip(requireContext());
        ((MainActivity) requireContext()).showSongProfile();
    }

    public void prev() {
        playbackManager.prev(requireContext());
        ((MainActivity) requireContext()).showSongProfile();
    }


    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            System.out.println("Success");
        } else {
            // You can directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.READ_MEDIA_AUDIO }, 123);

        }
    }


    public void updateUIElements(Boolean hasPermission){
        if(hasPermission){
            button.setVisibility(View.INVISIBLE);
            searchBar.setVisibility(View.VISIBLE);
            songList.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
            searchBar.setVisibility(View.INVISIBLE);
            songList.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            boolean granted = (grantResults.length > 0)
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED);

            updateUIElements(granted);
        }
    }

}


