package edu.uga.cs.themp3layer;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class PlaybackManager {

    public interface OnSongCompleteListener {
        void onSongComplete();
    }


    private OnSongCompleteListener listener;

    private static PlaybackManager instance;
    private MediaPlayer mediaPlayer;

    private Song currentSong;

    private ArrayList<Song> audioList;

    private PlaybackManager(Context context) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mediaplayer){
                skip(context);

                if (listener != null){
                    listener.onSongComplete();
                }
            }
        });
    }

    public static PlaybackManager getInstance(Context context) {
        if (instance == null) {
            instance = new PlaybackManager(context.getApplicationContext());
        }
        return instance;
    }

    public void play(Context context, Song song) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, song.getContentUri());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
            currentSong = song;
        } catch(IOException e) {
        throw new RuntimeException(e);
        }
    }

    public void pause (){
        mediaPlayer.pause();
    }


    public void resume (){
        mediaPlayer.start();
    }

    public void prev (Context context){
        Song currentlyPlaying = getCurrentSong();
        int songID = audioList.indexOf(currentlyPlaying);
        songID--;
        play(context, audioList.get(songID));
    }

    public void skip (Context context){
        Song currentlyPlaying = getCurrentSong();
        int songID = audioList.indexOf(currentlyPlaying);
        songID++;
        play(context, audioList.get(songID));
    }

    public void setListener(OnSongCompleteListener listener){
        this.listener = listener;
    }


    public Boolean getIsPlaying(){
        return mediaPlayer.isPlaying();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setAudioList (ArrayList<Song> list){
        audioList = list;
    }

}
