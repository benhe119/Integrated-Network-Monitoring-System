package automan.automanclientsmobile_android.VIEW.STARTUP_VIEWS;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.R;

public class Splash extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e("SPLASH ACTIVITY -> ", "Splash Screen Activity is running");
        this.img = (ImageView) findViewById(R.id.image);
        this.img.setImageResource(R.mipmap.company_logo); //  SET TO COMPANY IMAGE

        new Setup().execute();
    }

    private class Setup extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            // CHECK FOR ACCESS_TOKEN & USER OBJECT AND SEND TO EITHER Login OR Home ACTIVITY
            return AutomanApp.getApp().isLoggedIn();
        }

        protected void onPostExecute(Boolean res) {
            Log.e("Is User Logged in? -> ", res.toString());
            if (!res) startActivity(new Intent(Splash.this, Login.class));
            else AutomanApp.getApp().getData(); // GET DASH BOARD DATA
        }
    }
}
