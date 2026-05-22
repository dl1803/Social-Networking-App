package com.example.emptyproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    // hàm interface để bắt activity xử lí sự kiện khi click vào button
    // Do Adapter chỉ dùng để xử lí giao diện list item còn việc xử lí logic do activity thực hiện
    // (6)
    public interface OnUserActionListener {
        void onActionButtonClick(User user, int mode, boolean isPrimaryAction);
    }

    private Context context;
    private List<User> userList;
    private int mode;

    // biến listener sẽ bắt việc item bị click ở giao diện (Adapter) để báo dùng hàm onActionButtonClick báo cho activity xử lí logic
    private OnUserActionListener listener;

    public UserAdapter(Context context, List<User> userList, int mode, OnUserActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.mode = mode;// 1: Received, 2: Sent, 3: Friends, 4: Suggest
        this.listener = listener;
    }


    // hàm tạo view cho 1 item trong list
    // trả về 1 obj UserViewHolder quản lí các view của item đó
    // (1) tạo view thật trong Ram
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view); //(2) tạo viewholder quản lí view của item
    }

    // hàm gán dữ liệu cho item trong list
    // holder là item UI hiện tại (đang ch có data) với vị trí position ( item đang hiện thị trên screen)
    // (4) Đổ data vào item đó
    // (*) Recycler hoạt động tương tự queue khi holder nào ra khỏi screen trước thì sẽ được reuse cho data mới ở cuối màn hình (ví dụ : holder 2 -> ... -> holder 1)
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvUserName.setText(user.getName());

        if (mode == 1) {
            holder.btnUserAction.setText("Accept");
            holder.btnUserAction.setBackgroundColor(Color.parseColor("#28A745")); // Xanh lá
            holder.btnDecline.setVisibility(View.VISIBLE);
        } else {
            holder.btnDecline.setVisibility(View.GONE); // Giấu nút Từ chối ở các chế độ khác
            if (mode == 2) {
                holder.btnUserAction.setText("Cancel");
                holder.btnUserAction.setBackgroundColor(Color.parseColor("#DC3545")); // Đỏ
            } else if (mode == 3) {
                holder.btnUserAction.setText("Unfriend");
                holder.btnUserAction.setBackgroundColor(Color.parseColor("#6C757D")); // Xám
            } else if (mode == 4) {
                holder.btnUserAction.setText("Add Friend");
                holder.btnUserAction.setBackgroundColor(Color.parseColor("#007BFF")); // Xanh dương
            }
        }

        // (5) bắt sự kiện click vào button -> gọi interface để báo cho activity xử lí logic
        holder.btnUserAction.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionButtonClick(user, mode, true);
            }
        });

        holder.btnDecline.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionButtonClick(user, mode, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // hàm giữ các view bên trong(ví dụ các textview , button) của item trong list
    // -> trả về 1 obj UserViewHolder
    // ViewHolder giúp quản lí các view của item bằng cách lưu địa chỉ của các view vào Ram
    // -> giúp không cần gọi findViewById() nhiều lần gây tốn tài nguyên, tái sử dụng nhanh
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserPhone;
        Button btnUserAction, btnDecline;

        // itemView là toàn bộ giao diện của 1 item lấy từ inflate(R.layout.item_user)
        // (3) tìm các view của item đó rồi lưu lại địa chỉ
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnUserAction = itemView.findViewById(R.id.btnUserAction);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }
}