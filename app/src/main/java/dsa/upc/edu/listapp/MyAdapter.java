package dsa.upc.edu.listapp;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dsa.upc.edu.listapp.tracks.Song;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Song> values;
    private OnClickListener itemClickListener; // Add an item click listener
    public RecyclerView recyclerView; // Add this field to hold a reference to the RecyclerView

    // Add this method to get the item at a specific position
    public Song getItem(int position) {
        if (position >= 0 && position < values.size()) {
            return values.get(position);
        }
        return null;
    }
    // Add this method to set the item click listener
    public void setOnItemClickListener(OnClickListener listener) {
        this.itemClickListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            // Set click listener for the item view
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(view);
                    }
                }
            });

        }

    }


    public void setData(List<Song> myDataset) {
        values = myDataset;
        notifyDataSetChanged();
    }

    public void add(int position, Song item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    public MyAdapter() {
        this.recyclerView = recyclerView;
        values = new ArrayList<>();
    }
    //method to get the songID
    public TextView getFirstLineTextView(int position) {
        // Ensure the position is valid
        if (position >= 0 && position < getItemCount()) {
            // Get the ViewHolder for the specified position
            ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

            // Return the firstLine TextView if the ViewHolder is not null
            if (viewHolder != null) {
                return viewHolder.txtHeader;
            }
        }

        // Return null if the position is invalid or ViewHolder is null
        return null;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Song> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Song c = values.get(position);
        final String songID = c.id;
        holder.txtHeader.setText(songID);
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });

        holder.txtFooter.setText("Title: " + c.title +" "+"Singer: "+c.singer);
        // Set click listener for the txtHeader TextView
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }



}