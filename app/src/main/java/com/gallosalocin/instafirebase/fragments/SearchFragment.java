package com.gallosalocin.instafirebase.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gallosalocin.instafirebase.adapter.TagAdapter;
import com.gallosalocin.instafirebase.adapter.UserAdapter;
import com.gallosalocin.instafirebase.databinding.FragmentSearchBinding;
import com.gallosalocin.instafirebase.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private List<User> userList;
    private UserAdapter userAdapter;

    private List<String> hashTagsList;
    private List<String> hashTagsCountList;
    private TagAdapter tagAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerViewUsers.setHasFixedSize(true);
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.recyclerViewTags.setHasFixedSize(true);
        binding.recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        hashTagsList = new ArrayList<>();
        hashTagsCountList = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext(), hashTagsList, hashTagsCountList);
        binding.recyclerViewTags.setAdapter(tagAdapter);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userList, true);
        binding.recyclerViewUsers.setAdapter(userAdapter);

        readUsers();
        readTags();

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return view;
    }

    private void readTags() {
        FirebaseDatabase.getInstance().getReference().child("hashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hashTagsList.clear();
                hashTagsCountList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hashTagsList.add(snapshot.getKey());
                    hashTagsCountList.add(snapshot.getChildrenCount() + "");
                }
                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(binding.searchBar.getText().toString())) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        userList.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void filter(String text) {
        List<String> searchTags = new ArrayList<>();
        List<String> searchTagsCount = new ArrayList<>();

        for (String s : hashTagsList) {
            if (s.toLowerCase().contains(text.toLowerCase())) {
                searchTags.add(s);
                searchTagsCount.add(hashTagsCountList.get(hashTagsList.indexOf(s)));
            }
        }
        tagAdapter.filter(searchTags, searchTagsCount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
