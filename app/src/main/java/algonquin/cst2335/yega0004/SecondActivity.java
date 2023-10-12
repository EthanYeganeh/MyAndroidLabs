package algonquin.cst2335.yega0004;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);

        Intent fromPrecious = getIntent();
        String emailAddress = fromPrecious.getStringExtra("Email Address");
        Button changePic = findViewById(R.id.button3);
        TextView txtView = findViewById(R.id.textView3);
        txtView.setText("Welcome Back "+ emailAddress);
        EditText ed = findViewById(R.id.editTextPhone);
        Button callBtn = findViewById(R.id.button2);
        callBtn.setOnClickListener( clk -> {
            String phoneNumber = ed.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {

                @Override

                public void onActivityResult(ActivityResult result) {
                    ImageView profileImage = findViewById(R.id.imageView);
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        profileImage.setImageBitmap(thumbnail);
                    }


                }
            });
            changePic.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    cameraResult.launch(cameraIntent);
                }
            });





    }
}