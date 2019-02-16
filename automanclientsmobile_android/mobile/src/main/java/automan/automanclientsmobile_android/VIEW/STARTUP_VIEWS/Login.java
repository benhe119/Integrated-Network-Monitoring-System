package automan.automanclientsmobile_android.VIEW.STARTUP_VIEWS;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import automan.automanclientsmobile_android.AutomanApp;
import automan.automanclientsmobile_android.CONTROLLER.AccessAutomanAPI;
import automan.automanclientsmobile_android.R;

public class Login extends AppCompatActivity  implements View.OnClickListener {

    private EditText user;
    private EditText pass;
    private Button b1;
    private LinearLayout context;

    @Override
    protected void onResume() {
        super.onResume();
        this.user.setText("");
        this.pass.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = (LinearLayout) findViewById(R.id.context);

        this.context.setOnClickListener(this);

        this.user = (EditText) findViewById(R.id.loginname);
        this.pass = (EditText) findViewById(R.id.loginpass);

        this.b1 = (Button) findViewById(R.id.loginbutton);
        this.b1.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.loginbutton) {
            if (this.user.getText().length() < 1) {
                Toast t = Toast.makeText(this, "ENTER USERNAME", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                t = Toast.makeText(this, "OR CREATE NEW ACCOUNT ON DASHBOARD APP", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

            } else {
                if (this.pass.getText().length() < 1) {
                    Toast t = Toast.makeText(this, "ENTER PASSWORD", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                } else {
                    //CHECK AUTOMANAPI FOR CREDENTIALS,
                    try {
                        // MUST BE AN ASYNC TASK
                        (new LoginAsync()).execute(this.user.getText().toString(), this.pass.getText().toString());
                    } catch (Exception e) {
                        Log.e("Error->", e.toString());
                        Toast.makeText(Login.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (v.getId() == R.id.context) {
            //HIDE KEYBOARD WHEN YOU CLICK OUTSIDE THE EDITTEXT
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_login, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem it) {
//        if (it.getItemId() == R.id.somemenuitem) {
//        }
        return super.onOptionsItemSelected(it);
    }

    //////////////////////////////////////////////////////////////////
    /// AYNCHRONOUS TASKS HERE
    public class LoginAsync extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!AutomanApp.getApp().isOnline()) {
                Log.e("NOTICE", "NOT ONLINE");
                cancel(true);
                return;
            }
            AutomanApp.getApp().server().showProgress(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return AutomanApp.getApp().server().login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            AutomanApp.getApp().server().showProgress(false);
            if (res) {
                // GET ALL DATA BEFORE YOU SEND USER TO NEXT ACTIVITY
                AutomanApp.getApp().getData(); //    GET DASHBOARD DATA
            } else {
                AutomanApp.getApp().showToast("INCORRECT USERNAME OR PASSWORD");
                Login.this.user.setText("");
                Login.this.pass.setText("");
            }
        }
    }
}
