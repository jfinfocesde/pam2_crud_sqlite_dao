package com.example.sqlitep2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitep2.data.adapter.UserAdapter;
import com.example.sqlitep2.data.dao.ProductDao;
import com.example.sqlitep2.data.dao.UserDao;
import com.example.sqlitep2.data.db.DatabaseManager;
import com.example.sqlitep2.data.model.Product;
import com.example.sqlitep2.data.model.User;

import java.util.List;

public class UserListActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    private UserDao userDao;

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        userDao = new UserDao(dbManager.openDatabase());

        List<User> allUsers = userDao.getAllUsers();

        recyclerView = findViewById(R.id.recyclerView);
        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        adapter = new UserAdapter(allUsers);
        adapter.setOnItemClickListener(user -> {

            Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
            // Put user data into the intent
            intent.putExtra("USER_ID", user.getId());
            intent.putExtra("USERNAME", user.getUsername());
            intent.putExtra("EMAIL", user.getEmail());
            intent.putExtra("IMAGE_URL", user.getImageUrl());
            // Start the EditUserActivity
            startActivity(intent);

        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
    }
}