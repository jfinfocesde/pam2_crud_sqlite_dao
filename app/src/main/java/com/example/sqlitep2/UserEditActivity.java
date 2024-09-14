package com.example.sqlitep2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlitep2.data.dao.ProductDao;
import com.example.sqlitep2.data.dao.UserDao;
import com.example.sqlitep2.data.db.DatabaseManager;
import com.example.sqlitep2.data.model.User;

public class UserEditActivity extends AppCompatActivity {

    private static final String TAG = "OUT_USER_EDIT";
    DatabaseManager dbManager;
    private UserDao userDao;
    private ProductDao productDao;

    TextView editTextUsername, editTextEmail, editTextImageUrl;
    Button buttonUpdateUser;

    private long userId;
    private String username;
    private String email;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        userDao = new UserDao(dbManager.openDatabase());

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        buttonUpdateUser = findViewById(R.id.buttonUpdateUser);

        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        username = intent.getStringExtra("USERNAME");
        email = intent.getStringExtra("EMAIL");
        imageUrl = intent.getStringExtra("IMAGE_URL");

        editTextUsername.setText(username);
        editTextEmail.setText(email);
        editTextImageUrl.setText(imageUrl);

        buttonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setId(userId);
                user.setUsername(editTextUsername.getText().toString());
                user.setEmail(editTextEmail.getText().toString());
                user.setImageUrl(editTextImageUrl.getText().toString());
                userDao.update(user);
                Intent intent = new Intent(UserEditActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
    }
}