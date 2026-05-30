package com.example.emptyproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        ImageView btnBack = findViewById(R.id.btnBackFriend);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

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

        User fakeUser1 = new User("Hacker", "hacker88@gmail.com", "123", "0909111");
        fakeUser1.setAvatarUrl("https://tse3.mm.bing.net/th/id/OIP.Ns8jniA5oxRt4_CqSOCgigHaHa?cb=thfc1falcon&rs=1&pid=ImgDetMain&o=7&rm=3"); // mock ảnh trên mạng
        sentList.add(fakeUser1);

        User fakeFriend2 = new User("Vũ", "VuAVu@gmail.com", "123", "0909222");
        fakeFriend2.setAvatarUrl("https://tse3.mm.bing.net/th/id/OIP.-OnBoYX4HOKMGx6s1wQH3AHaHw?cb=thfc1falcon&w=840&h=880&rs=1&pid=ImgDetMain&o=7&rm=3");
        friendList.add(fakeFriend2);

        User fakeUser3 = new User("Người bí ẩn", "user@gmail.com", "123", "0909333");
        fakeUser3.setAvatarUrl("https://cdn3.iconfinder.com/data/icons/diverse-cartoon-women-avatars/300/20-512.png");
        receivedList.add(fakeUser3);

        setupAdapters();
        // tạm tắt api lấy danh sách bbe vì chưa có api add friend, mock data để check UI
//        if (LoginActivity.currentUser != null) {
//            fetchFriendList(LoginActivity.currentUser.getId());
//        }
    }

    private void setupAdapters() {
        receivedAdapter = new UserAdapter(this, receivedList, 1, (targetUser, mode, isPrimaryAction) -> {
            if (isPrimaryAction) {
                // Logic ĐỒNG Ý
                receivedList.remove(targetUser);
                friendList.add(targetUser);
                Toast.makeText(this, "Đã chấp nhận kết bạn!", Toast.LENGTH_SHORT).show();
            } else {
                // Logic TỪ CHỐI
                receivedList.remove(targetUser);
                Toast.makeText(this, "Đã từ chối lời mời!", Toast.LENGTH_SHORT).show();
            }
            notifyAllAdapters();
        });

        sentAdapter = new UserAdapter(this, sentList, 2, (targetUser, mode, isPrimaryAction) -> {
            sentList.remove(targetUser);
            notifyAllAdapters();
            Toast.makeText(this, "Đã hủy lời mời!", Toast.LENGTH_SHORT).show();
        });

        friendsAdapter = new UserAdapter(this, friendList, 3, (targetUser, mode, isPrimaryAction) -> {
            friendList.remove(targetUser);
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

    private void fetchFriendList(int userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getFriends(userId).enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FriendListResponse friendResponse = response.body();

                    if ("success".equals(friendResponse.getStatus())) {
                        friendList.clear();
                        List<User> serverFriends = friendResponse.getFriends();

                        if (serverFriends != null) {
                            friendList.addAll(serverFriends);
                        }

                        if (friendsAdapter != null) {
                            friendsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                Toast.makeText(FriendManagerActivity.this, "Lỗi tải bạn bè: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receivedAdapter != null) notifyAllAdapters();
    }
}