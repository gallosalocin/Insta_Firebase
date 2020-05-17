package com.gallosalocin.instafirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gallosalocin.instafirebase.CommentActivity;
import com.gallosalocin.instafirebase.R;
import com.gallosalocin.instafirebase.databinding.PostItemBinding;
import com.gallosalocin.instafirebase.fragments.PostDetailFragment;
import com.gallosalocin.instafirebase.fragments.ProfileFragment;
import com.gallosalocin.instafirebase.model.Post;
import com.gallosalocin.instafirebase.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding binding = PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        Picasso.get().load(post.getImageUrl()).into(holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageUrl().equals("default")) {
                    holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageUrl()).into(holder.imageProfile);
                }
                holder.username.setText(user.getUsername());
                holder.author.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isLiked(post.getPostId(), holder.like);
        nbrOfLikes(post.getPostId(), holder.nbrOfLikes);
        getComments(post.getPostId(), holder.nbrOfComments);
        isSaved(post.getPostId(), holder.save);


        holder.like.setOnClickListener(view -> {
            if (holder.like.getTag().equals("like")) {
                FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostId()).child(firebaseUser.getUid()).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostId()).child(firebaseUser.getUid()).removeValue();

            }
        });

        holder.comment.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("authorId", post.getPublisher());
            context.startActivity(intent);
        });

        holder.nbrOfComments.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("authorId", post.getPublisher());
            context.startActivity(intent);
        });

        holder.save.setOnClickListener(view -> {
            if (holder.save.getTag().equals("save")){
                FirebaseDatabase.getInstance().getReference().child("saves")
                        .child(firebaseUser.getUid()).child(post.getPostId()).setValue(true);
            }else {
                FirebaseDatabase.getInstance().getReference().child("saves")
                        .child(firebaseUser.getUid()).child(post.getPostId()).removeValue();
            }
        });

        holder.imageProfile.setOnClickListener(view -> {
            context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                    .edit().putString("profileId", post.getPublisher()).apply();

            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        holder.username.setOnClickListener(view -> {
            context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                    .edit().putString("profileId", post.getPublisher()).apply();

            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        holder.author.setOnClickListener(view -> {
            context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                    .edit().putString("profileId", post.getPublisher()).apply();

            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        holder.postImage.setOnClickListener(view -> {
            context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postId", post.getPostId()).apply();

            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PostDetailFragment()).commit();
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageProfile;
        AppCompatTextView username;
        AppCompatImageView more;
        AppCompatImageView postImage;
        AppCompatImageView like;
        AppCompatImageView comment;
        AppCompatImageView save;
        AppCompatTextView nbrOfLikes;
        AppCompatTextView author;
        AppCompatTextView nbrOfComments;
        SocialTextView description;


        public ViewHolder(@NonNull PostItemBinding binding) {
            super(binding.getRoot());

            imageProfile = binding.profileImage;
            username = binding.username;
            more = binding.more;
            postImage = binding.postImage;
            like = binding.like;
            comment = binding.comment;
            save = binding.save;
            nbrOfLikes = binding.nbrOfLikes;
            nbrOfComments = binding.nbrOfComments;
            author = binding.author;
            description = binding.description;
        }
    }

    private void isSaved(String postId, AppCompatImageView image) {
        FirebaseDatabase.getInstance().getReference().child("saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()){
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                }else{
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void isLiked(String postId, AppCompatImageView image) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    image.setImageResource(R.drawable.ic_liked);
                    image.setTag("liked");
                } else {
                    image.setImageResource(R.drawable.ic_like);
                    image.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nbrOfLikes(String postId, AppCompatTextView text) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, AppCompatTextView text) {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
