package com.inochi.music.player.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inochi.music.player.R;
import com.inochi.music.player.helper.Constants;
import com.inochi.music.player.item.FileItem;
import com.inochi.music.player.listener.FileListener;

import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private final ArrayList<FileItem> items;
	private FileListener fileListener;

	public ExploreAdapter(ArrayList<FileItem> items, FileListener fileListener) {
		this.items = items;
		this.fileListener = fileListener;
	}

	@NonNull
    @Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.card_file, parent, false);
		return new BodyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
								 @SuppressLint("RecyclerView") final int position) {
		final BodyViewHolder holder = (BodyViewHolder) viewHolder;
		final FileItem fileItem = items.get(position);
		int myIcon = fileItem.getIcon();

		if (myIcon > 0){
			holder.imgIcon.setImageResource(myIcon);
		}

		holder.tvwTitle.setText(fileItem.getTitle());
		holder.tvwDescription.setText(fileItem.getPath());

        if (fileItem.getType() == Constants.GroupType.TYPE_PARENT){
            holder.chkSelect.setVisibility(View.INVISIBLE);
        } else {
            holder.chkSelect.setVisibility(View.VISIBLE);
        }

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                fileListener.setOnFileItemClick(view, fileItem);
                //if (fileItem.getType() == Constant.GroupType.TYPE_FILE){
                if (fileItem.getType() != Constants.GroupType.TYPE_PARENT){
                    holder.chkSelect.setChecked(!holder.chkSelect.isChecked());
                }
                //}
			}
		});

        holder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fileItem.setSelect(isChecked);
                items.set(position, fileItem);
            }
        });
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getType();
	}

	public ArrayList<FileItem> getListItems(){
        return items;
    }

	public class BodyViewHolder extends RecyclerView.ViewHolder {
		public final View itemView;
		public final TextView tvwTitle;
		public final TextView tvwDescription;
		public final ImageView imgIcon;
		public final CheckBox chkSelect;

		public BodyViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			this.tvwTitle = (TextView) itemView.findViewById(R.id.tvwTitle);
			this.tvwDescription = (TextView) itemView.findViewById(R.id.tvwDescription);
			this.imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
			this.chkSelect = (CheckBox) itemView.findViewById(R.id.chkSelect);
		}
	}

}
