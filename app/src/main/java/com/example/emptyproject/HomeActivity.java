package com.example.emptyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    ArrayList<Post> displayList = new ArrayList<>();
    PostAdapter adapter;
    boolean isSortDateASC = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        EditText edtIdea = findViewById(R.id.edtIdea);
        TextView btnPost = findViewById(R.id.btnPost);
        ListView lvIdea = findViewById(R.id.lvIdea);

        adapter = new PostAdapter(this, R.layout.item_status, displayList);
        lvIdea.setAdapter(adapter);

        registerForContextMenu(lvIdea); // đki context menu cho lvIdea

        fetchAllPosts();

        btnPost.setOnClickListener(v -> {
            String content = edtIdea.getText().toString().trim();

            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung bài viết!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (LoginActivity.currentUser == null) {
                Toast.makeText(this, "Vui lòng đăng nhập để đăng bài!", Toast.LENGTH_SHORT).show();
                return;
            }

            Post newPost = new Post(LoginActivity.currentUser.getId(), content);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.createPost(newPost).enqueue(new Callback<SinglePostResponse>() {
                @Override
                public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SinglePostResponse postResponse = response.body();

                        if ("success".equals(postResponse.getStatus())) {
                            Toast.makeText(HomeActivity.this, "Đã đăng bài viết mới!", Toast.LENGTH_SHORT).show();

                            edtIdea.setText("");

                            fetchAllPosts();

                        } else {
                            Toast.makeText(HomeActivity.this, postResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Lỗi đăng bài. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        lvIdea.setOnItemClickListener((parent, view, position, id) -> {
            Post clickedPost = displayList.get(position);

            if (clickedPost.getAuthor() != null) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("user_id", clickedPost.getAuthor().getId());
                startActivity(intent);
            }
        });
    }

    private void fetchAllPosts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getAllPosts().enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostListResponse postResponse = response.body();

                    if ("success".equals(postResponse.getStatus())) {

                        displayList.clear();
                        List<Post> serverPosts = postResponse.getData();

                        if (serverPosts != null) {
                            for (Post p : serverPosts) {
                                boolean isHidden = false;
                                if (LoginActivity.currentUser != null) {
                                    for (Post hiddenPost : LoginActivity.currentUser.getMyHiddenList()) {
                                        if (hiddenPost.getId() == p.getId()) {
                                            isHidden = true;
                                            break;
                                        }
                                    }
                                }
                                if (!isHidden) {
                                    displayList.add(p);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(HomeActivity.this, "Lỗi lấy bài viết!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.opt_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.opt_sort_author) {
            Collections.sort(displayList, new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    String name1 = p1.getAuthor() != null ? p1.getAuthor().getName() : "";
                    String name2 = p2.getAuthor() != null ? p2.getAuthor().getName() : "";
                    return name1.compareToIgnoreCase(name2);
                }
            });
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã sắp xếp theo Author", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.opt_sort_date) {
            Collections.sort(displayList, new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    String date1 = p1.getCreatedAt() != null ? p1.getCreatedAt() : "";
                    String date2 = p2.getCreatedAt() != null ? p2.getCreatedAt() : "";

                    if (isSortDateASC) {
                        return date1.compareTo(date2);
                    } else {
                        return date2.compareTo(date1);
                    }
                }
            });
            isSortDateASC = !isSortDateASC;
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã sắp xếp theo Ngày", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_suggest_friend) {
            Intent intent = new Intent(this, SuggestFriendActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_friend_manager) {
            Intent intent = new Intent(this, FriendManagerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvIdea) {
            getMenuInflater().inflate(R.menu.contexts_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // item.getMenuInfo() : Khi nhấn giữ một item trong ListView, hệ thống gửi kèm theo thông tin về item đó vào menu (position, id, view)
        // Kết quả trả về Object (đối tượng tổng quát) , chưa cụ thể là đối tượng cụ thể nào (do MenuItem.getMenuInfo() dùng chung cho nhiều loại View khác)
        // Do đó, phải ép kiểu về đối tượng cụ thể  (AdapterView.AdapterContextMenuInfo) : object chứa thông tin của item bị nhấn giữ trong ListView
        // class này chứa : - info.position (vị trí item)   - //info.id (id item)     - //info.targetView (view bị click)
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Post selectedPost = displayList.get(position);

        int id = item.getItemId();

        if (id == R.id.ctx_detail) {
            Intent intent = new Intent(HomeActivity.this, PostDetailActivity.class);
            String authorName = selectedPost.getAuthor() != null ? selectedPost.getAuthor().getName() : "Unknown";

            intent.putExtra("detail_name", authorName);
            intent.putExtra("detail_date", selectedPost.getCreatedAt());
            intent.putExtra("detail_content", selectedPost.getContent());
            startActivity(intent);
            return true;
        }
        else if (id == R.id.ctx_hide) {
            if (LoginActivity.currentUser != null) {
                LoginActivity.currentUser.getMyHiddenList().add(selectedPost);
                displayList.remove(selectedPost);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Đã ẩn bài viết với bạn!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
