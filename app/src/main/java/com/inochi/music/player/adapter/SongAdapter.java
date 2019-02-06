package com.inochi.music.player.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inochi.music.player.R;
import com.inochi.music.player.item.SongItem;
import com.inochi.music.player.listener.SongListener;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
	private ArrayList<SongItem> items;
	private SongListener listener;

	public SongAdapter(ArrayList<SongItem> items, SongListener listener) {
		this.items = items;
		this.listener = listener;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.card_song, parent, false);
		return new BodyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
		final BodyViewHolder holder = (BodyViewHolder) viewHolder;
		final SongItem item = items.get(position);

		if (item != null){
			String title = item.getSongTitle();
			String artist = item.getSongArtist();

			holder.tvwSongTitle.setText(title);
			holder.tvwSongArtist.setText(artist);

			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onSongItemClick(v, item);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public ArrayList<SongItem> getItems(){
		return this.items;
	}

	public void setItems(ArrayList<SongItem> items){
		this.items = items;
	}

	public class BodyViewHolder extends RecyclerView.ViewHolder {
		public final View itemView;
		public final TextView tvwSongTitle;
		public final TextView tvwSongArtist;
		public final ImageView imgSongIcon;

		public BodyViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			this.tvwSongTitle = itemView.findViewById(R.id.tvwSongTitle);
			this.tvwSongArtist = itemView.findViewById(R.id.tvwSongArtist);
			this.imgSongIcon = itemView.findViewById(R.id.imgSongIcon);
		}
	}

}
