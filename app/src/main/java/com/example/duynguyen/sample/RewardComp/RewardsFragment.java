package com.example.duynguyen.sample.RewardComp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duynguyen.sample.R;
import com.example.duynguyen.sample.model.CloudImage;
import com.example.duynguyen.sample.model.Student;
import com.example.duynguyen.sample.model.User;
import com.example.duynguyen.sample.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class RewardsFragment extends Fragment {

    @BindView(R.id.reward_rv)
    RecyclerView rewardRv;

    private User mCurrentUser;
    private RewardItemsAdapter mRIAdapter;
    private DatabaseReference mRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        ButterKnife.bind(this,view);

        //set Fb
        mRef = FirebaseDatabase.getInstance().getReference();
        //setup Rv
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mRIAdapter = new RewardItemsAdapter();
        rewardRv.setAdapter(mRIAdapter);
        rewardRv.setLayoutManager(gridLayoutManager);

        setUpview();

        return view;
    }

    private void setUpview() {
        mCurrentUser = getCurrentUserInfo();
        String userType = mCurrentUser.getUserType();
        if (userType.equals(Utils.STUDENT)) {
            mRef.child(Utils.USERS_CHILD).child(mCurrentUser.getfUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);
                    List<CloudImage> rewardItems = student.getRewardPics();
                    mRIAdapter.setmRewardItems(rewardItems);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //if current username is teacher
        else {

        }

    }


    private User getCurrentUserInfo() {
        final SharedPreferences mPrefs = Objects.requireNonNull(getActivity()).getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(Utils.CURRENT_USER_KEY, "");
        return gson.fromJson(json, User.class);
    }
}
