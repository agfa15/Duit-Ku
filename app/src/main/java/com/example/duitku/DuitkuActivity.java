package com.example.duitku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DuitkuActivity extends AppCompatActivity {

    private TextView totalDuitku;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    private DatabaseReference duitkuRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private String post_key = "";
    private String nama = "";
    private int ammount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duitku);

        mAuth = FirebaseAuth.getInstance();
        duitkuRef = FirebaseDatabase.getInstance().getReference().child("duitku").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);

        totalDuitku = findViewById(R.id.totalDuitku);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        duitkuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalDuit = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    totalDuit += data.getAmmount();
                    String sTotal = String.valueOf("Rp " +totalDuit);
                    totalDuitku.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    private void addItem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

//        final Spinner itemSpinner = myView.findViewById(R.id.itemspinner);
        final EditText ammount = myView.findViewById(R.id.ammount);
        final EditText nama = myView.findViewById(R.id.nama);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String budgetAmmount = ammount.getText().toString();
                String namaItem = nama.getText().toString();
//                String budgetItem = itemSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(budgetAmmount)){
                    ammount.setError("Nominal harus di isi");
                    return;
                }
                if(TextUtils.isEmpty(namaItem)){
                    nama.setError("Nama harus diisi");
                    return;
                }
//                if (budgetItem.equals("Select Item")){
//                    Toast.makeText(DuitkuActivity.this, "Pilih item yang beanar", Toast.LENGTH_SHORT).show();
//                }
                else {
                    loader.setMessage("Tambahkan item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = duitkuRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epoch, now);

                    Data data = new Data( namaItem, date, id, null, Integer.parseInt(budgetAmmount), months.getMonths());
                    duitkuRef.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DuitkuActivity.this, "Biaya item berhasil di masukan", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DuitkuActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(duitkuRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.setItemNama("" + model.getNama());
                holder.setItemAmmount("Rp "+ model.getAmmount());
                holder.setDate(""+model.getDate());
                holder.imageView.setImageResource(R.drawable.budget);
//                holder.setItemName(""+model.getItem());

                holder.deskripsi.setVisibility(View.GONE);

//                switch (model.getItem()){
//                    case "Belanja Umum":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Makanan":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Transportasi":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Tagihan Listrik":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Paket Data":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Kesehatan":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Sekolah":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                    case "Lainnya":
//                        holder.imageView.setImageResource(R.drawable.budget);
//                        break;
//                }
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        nama = model.getNama();
                        ammount = model.getAmmount();
                        updateData();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            View mView;
            public ImageView imageView;
            public TextView deskripsi, date;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;
                imageView = itemView.findViewById(R.id.imageView);
                deskripsi = itemView.findViewById(R.id.deskripsi);
                date = itemView.findViewById(R.id.date);

            }

            public void setItemNama (String itemNama){
                TextView nama = mView.findViewById(R.id.nama);
                nama.setText(itemNama);
            }
            public void setItemAmmount (String itemAmmount){
                TextView ammount = mView.findViewById(R.id.ammount);
                ammount.setText(itemAmmount);
            }
            public void setDate (String itemDate){
                TextView date = mView.findViewById(R.id.date);
                date.setText(itemDate);
            }
        }

        private void updateData(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        final TextView mNama = mView.findViewById(R.id.namaa);
        final EditText mAmmount = mView.findViewById(R.id.ammount);
        final EditText mDeskripsi = mView.findViewById(R.id.itemDeskripsi);

        mDeskripsi.setVisibility(View.GONE);

        mNama.setText(nama);

        mAmmount.setText(String.valueOf(ammount));
        mAmmount.setSelection(String.valueOf(ammount).length());

        Button btnDelete = mView.findViewById(R.id.delete);
        Button btnUpdate = mView.findViewById(R.id.update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ammount = Integer.parseInt(mAmmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epoch, now);

                Data data = new Data( nama, date, post_key, null, ammount, months.getMonths());
                duitkuRef.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DuitkuActivity.this, "Berhasil Update Data", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DuitkuActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duitkuRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DuitkuActivity.this, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DuitkuActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });


        dialog.show();
        }
}