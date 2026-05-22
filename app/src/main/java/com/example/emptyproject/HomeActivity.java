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
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    public static ArrayList<Status> publicStatusList = new ArrayList<>();
    ArrayList<Status> displayList = new ArrayList<>();
    StatusAdapter adapter;
    boolean isSortDateASC = true;

    String currentName = "";
    String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (publicStatusList.isEmpty()) {
            publicStatusList.add(new Status("Alice", "Hello ngày mới!", "alice@gmail.com", "20/03/2026"));
            publicStatusList.add(new Status("Bob", "Một ngày tốt lành!", "bob@gmail.com", "22/03/2026"));
            publicStatusList.add(new Status("Alice", "Hôm nay ăn gì nhỉ?", "alice@gmail.com", "25/03/2026"));
        }

        EditText edtIdea = findViewById(R.id.edtIdea);
        TextView btnPost = findViewById(R.id.btnPost);
        ListView lvIdea = findViewById(R.id.lvIdea);

        Intent intentFromLogin = getIntent();
        currentName = intentFromLogin.getStringExtra("name");
        currentEmail = intentFromLogin.getStringExtra("email");

        adapter = new StatusAdapter(this, R.layout.item_status, displayList);
        lvIdea.setAdapter(adapter);

        registerForContextMenu(lvIdea); // đki context menu cho lvIdea

        refreshDisplayList();

        btnPost.setOnClickListener(v -> {
            String content = edtIdea.getText().toString().trim();
            if (!content.isEmpty()) {
                String nameToDisplay = currentName != null ? currentName : "Guest";

                Status newStatus = new Status(nameToDisplay, content, currentEmail);
                publicStatusList.add(newStatus);

                refreshDisplayList();
                edtIdea.setText("");
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
            }
        });

        lvIdea.setOnItemClickListener((parent, view, position, id) -> {
            Status clickedStatus = displayList.get(position);

            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("name", clickedStatus.getName());
            intent.putExtra("email", clickedStatus.getEmail());

            startActivity(intent);
        });
    }

    private void refreshDisplayList() {
        displayList.clear();
        for (Status s : publicStatusList) {
            if (LoginActivity.currentUser != null && !LoginActivity.currentUser.getMyHiddenList().contains(s)) {
                displayList.add(s);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
            intent.putExtra("name", currentName);
            intent.putExtra("email", currentEmail);
            startActivity(intent);
            return true;
        } else if (id == R.id.opt_sort_author) {
            Collections.sort(displayList, new Comparator<Status>() {
                @Override
                public int compare(Status s1, Status s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã sắp xếp theo Author", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.opt_sort_date) {
            Collections.sort(displayList, new Comparator<Status>() {
                @Override
                public int compare(Status s1, Status s2) {
                    // Buộc dùng try - catch vì sử dụng hàm parse() để convert String -> Date có thể gây ra lỗi nếu String không đúng định dạng
                    try {
                        // SimpleDateFormat (Định dạng thời gian)
                        // Bước này giúp hệ thống hiểu cấu trúc định dạng ngày tháng năm hiện tại(String) của obj s1, s2 -> để convert thành timestamp -> tạo nên Date
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date1 = sdf.parse(s1.getDate());
                        Date date2 = sdf.parse(s2.getDate());

                        if (isSortDateASC) {
                            return date1.compareTo(date2);
                        } else {
                            return date2.compareTo(date1);
                        }
                    } catch (Exception e) {
                        return 0; // Lỗi -> không thay đổi thứ tự (==0)
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
        Status selectedStatus = displayList.get(position);

        int id = item.getItemId();

        if (id == R.id.ctx_detail) {
            Intent intent = new Intent(HomeActivity.this, StatusDetailActivity.class);
            intent.putExtra("detail_name", selectedStatus.getName());
            intent.putExtra("detail_date", selectedStatus.getDate());
            intent.putExtra("detail_content", selectedStatus.getContent());
            startActivity(intent);
            return true;
        }
        else if (id == R.id.ctx_hide) {
            if (LoginActivity.currentUser != null) {
                LoginActivity.currentUser.getMyHiddenList().add(selectedStatus);
                refreshDisplayList();
                Toast.makeText(this, "Đã ẩn bài viết với bạn!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
