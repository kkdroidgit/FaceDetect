package android.paperwrrk.www.facedetect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button_process);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                detectFace(options);
            }
        });
        imageView = findViewById(R.id.imgView);
    }

    private void detectFace(BitmapFactory.Options options) {
        Bitmap bitmap = BitmapFactory.decodeResource(
                this.getResources(), R.drawable.test1,
                options
        );
        Paint paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);

        FaceDetector faceDetector = new FaceDetector.Builder(this).setTrackingEnabled(false)
                .build();
        if(!faceDetector.isOperational()){
            new AlertDialog.Builder(this).setMessage("Could not set up Face Detector!").show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        for(int i=0; i<faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            canvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, paint);
        }
        imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
    }
}
