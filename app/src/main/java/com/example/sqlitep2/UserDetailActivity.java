package com.example.sqlitep2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sqlitep2.data.dao.ProductDao;
import com.example.sqlitep2.data.dao.UserDao;
import com.example.sqlitep2.data.db.DatabaseManager;
import com.example.sqlitep2.data.model.User;

public class UserDetailActivity extends AppCompatActivity {
    private static final String TAG = "OUT_USER_EDIT";
    DatabaseManager dbManager;
    private UserDao userDao;
    private ProductDao productDao;

    TextView textViewUsername, textViewEmail;
    ImageView imageViewUser;
    Button buttonUpdateUser, buttonDeleteUser;

    private long userId;
    private String username;
    private String email;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        userDao = new UserDao(dbManager.openDatabase());

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        imageViewUser = findViewById(R.id.imageViewUser);
        buttonUpdateUser = findViewById(R.id.buttonUpdateUser);
        buttonDeleteUser = findViewById(R.id.buttonDeleteUser);

        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        username = intent.getStringExtra("USERNAME");
        email = intent.getStringExtra("EMAIL");
        imageUrl = intent.getStringExtra("IMAGE_URL");

        textViewUsername.setText(username);
        textViewEmail.setText(email);

        loadUserImage(imageUrl);

        buttonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailActivity.this, UserEditActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USERNAME", username);
                intent.putExtra("EMAIL", email);
                intent.putExtra("IMAGE_URL", imageUrl);
                startActivity(intent);
            }
        });

        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    private void loadUserImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewUser);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Estás seguro de que quieres realizar esta acción?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        userDao.deleteAllUsers();
                        Intent intent = new Intent(UserDetailActivity.this, UserListActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acción a realizar si el usuario cancela
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}