package ph.edu.uic.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import ph.edu.uic.foodapp.model.User;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Waiting");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child(edtPhone.getText().toString()).exists()) {

                            mDialog.dismiss();
                            User user = snapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(SignInActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                userisLoggedIn();
                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                    }
                        else {
                                mDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                             }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
    private void userisLoggedIn() {
        FirebaseDatabase data= FirebaseDatabase.getInstance();
        if(data != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }
}