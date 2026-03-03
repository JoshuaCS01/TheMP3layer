package edu.uga.cs.themp3layer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SListAdapter extends RecyclerView.Adapter<SListAdapter.ViewHolder> {

    private ArrayList<Song> songList;
    private final Context context;

    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onItemClick(Song song);
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.clickListener = l; }

    public SListAdapter(ArrayList<Song> list, Context context) {
        this.songList = list;
        this.context = context;
    }

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView songTitle;
            private final TextView songArtist;

            public ViewHolder(View view) {
                super(view);
                songTitle = view.findViewById(R.id.songName);
                songArtist = view.findViewById(R.id.songArtist);
            }

        }

        /**
         * Initialize the dataset of the Adapter
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView
         */


        // Create new views (invoked by the layout manager)
        @Override
        public SListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.song_card, parent, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(SListAdapter.ViewHolder holder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Song songData = songList.get(position);
            holder.songTitle.setText(songData.getName());
            holder.songArtist.setText(songData.getArtist());

            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onItemClick(songData);
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return songList.size();

        }

    }
