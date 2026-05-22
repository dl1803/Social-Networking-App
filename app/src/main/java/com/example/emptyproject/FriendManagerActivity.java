package com.example.emptyproject;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendManagerActivity extends AppCompatActivity {

    RecyclerView rvReceivedRequests, rvSentRequests, rvFriends;

    UserAdapter receivedAdapter, sentAdapter, friendsAdapter;

    ArrayList<User> receivedList;
    ArrayList<User> sentList;
    ArrayList<User> friendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);

        rvReceivedRequests = findViewById(R.id.rvReceivedRequests);
        rvSentRequests = findViewById(R.id.rvSentRequests);
        rvFriends = findViewById(R.id.rvFriends);

        rvReceivedRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSentRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (LoginActivity.currentUser != null) {
            receivedList = LoginActivity.currentUser.getReceivedRequests();
            sentList = LoginActivity.currentUser.getSentRequests();
            friendList = LoginActivity.currentUser.getFriendList();
        } else {
            receivedList = new ArrayList<>();
            sentList = new ArrayList<>();
            friendList = new ArrayList<>();
        }

        setupAdapters();
    }

    private void setupAdapters() {
        receivedAdapter = new UserAdapter(this, receivedList, 1, (targetUser, mode, isPrimaryAction) -> {
            if (isPrimaryAction) {
                // Logic ĐỒNG Ý
                receivedList.remove(targetUser);
                friendList.add(targetUser);
                targetUser.getSentRequests().remove(LoginActivity.currentUser);
                targetUser.getFriendList().add(LoginActivity.currentUser);
                Toast.makeText(this, "Đã chấp nhận kết bạn!", Toast.LENGTH_SHORT).show();
            } else {
                // Logic TỪ CHỐI
                receivedList.remove(targetUser);
                targetUser.getSentRequests().remove(LoginActivity.currentUser);
                Toast.makeText(this, "Đã từ chối lời mời!", Toast.LENGTH_SHORT).show();
            }
            notifyAllAdapters();
        });

        sentAdapter = new UserAdapter(this, sentList, 2, (targetUser, mode, isPrimaryAction) -> {
            sentList.remove(targetUser);
            targetUser.getReceivedRequests().remove(LoginActivity.currentUser);

            notifyAllAdapters();
            Toast.makeText(this, "Đã hủy lời mời!", Toast.LENGTH_SHORT).show();
        });

        friendsAdapter = new UserAdapter(this, friendList, 3, (targetUser, mode, isPrimaryAction) -> {
            friendList.remove(targetUser);
            targetUser.getFriendList().remove(LoginActivity.currentUser);

            notifyAllAdapters();
            Toast.makeText(this, "Đã hủy kết bạn!", Toast.LENGTH_SHORT).show();
        });

        rvReceivedRequests.setAdapter(receivedAdapter);
        rvSentRequests.setAdapter(sentAdapter);
        rvFriends.setAdapter(friendsAdapter);
    }

    private void notifyAllAdapters() {
        receivedAdapter.notifyDataSetChanged();
        sentAdapter.notifyDataSetChanged();
        friendsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receivedAdapter != null) notifyAllAdapters();
    }
}