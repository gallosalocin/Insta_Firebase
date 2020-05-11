package com.gallosalocin.instafirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gallosalocin.instafirebase.databinding.TagItemBinding;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private Context context;
    private List<String> tagsList;
    private List<String> tagsCountList;

    public TagAdapter(Context context, List<String> tagsList, List<String> tagsCountList) {
        this.context = context;
        this.tagsList = tagsList;
        this.tagsCountList = tagsCountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TagItemBinding binding = TagItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tag.setText("# " + tagsList.get(position));
        holder.nbrOfPosts.setText(tagsCountList.get(position) + " posts");
    }


    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tag;
        AppCompatTextView nbrOfPosts;

        ViewHolder(@NonNull TagItemBinding binding) {
            super(binding.getRoot());

            tag = binding.hashTag;
            nbrOfPosts = binding.nbrOfPosts;
        }
    }

    public void filter(List<String> filterTags, List<String> filterTagsCount){
        this.tagsList = filterTags;
        this.tagsCountList = filterTagsCount;

        notifyDataSetChanged();
    }
}
