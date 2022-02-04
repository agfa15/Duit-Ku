package com.example.duitku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView duitkuCardView, harianCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        duitkuCardView = findViewById(R.id.duitkuCardView);
        harianCardView = findViewById(R.id.harianCardView);

        duitkuCardView.setOnClickListener((view)-> {
            Intent intent = new Intent(MainActivity.this, DuitkuActivity.class);
            startActivity(intent);
        });

        harianCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HarianActivity.class);
                startActivity(intent);
            }
        });

    }
}
