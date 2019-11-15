package fr.eurecom.hellopreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    android.content.SharedPreferences preferences;
    // Depends on SDK: you can also use SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button button1 = (Button) findViewById(R.id.showPreferences);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = preferences.getString("username",
                        "n/a");
                String password = preferences.getString("password",
                        "n/a");
                // A toast is a view containing a quick little message for the user.
                showPrefs(username, password);
            }
        });
    }

    private void showPrefs(String username, String password){
        if (username.equals("n/a") && password.equals("n/a")){
            Toast.makeText(MainActivity.this,
                    "Your username and password haven't been set yet",
                    Toast.LENGTH_LONG).show();
        } else if (username.equals("n/a")){
            Toast.makeText(MainActivity.this,
                    "Your password is: " + password + "\nYour username hasn't been set yet",
                    Toast.LENGTH_LONG).show();
        } else if (password.equals("n/a")){
            Toast.makeText(MainActivity.this,
                    "Your username is: " + username + "\nYour password hasn't been set yet",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,
                "Your username and password: \nusername:" + username + " \npassword: " + password,
                Toast.LENGTH_LONG).show();
        }
    }

    public void openDialog(View v) {
        // Create out AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset username and password?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes, please", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Resetting Credentials",Toast.LENGTH_LONG).show();
                reset_preferences();
            }
        });

        builder.setNegativeButton("I'd rather not", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Kept Credentials",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reset_preferences(){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", null);
        edit.putString("password", null);
        edit.commit(); // Apply changes
// A toast is a view containing a quick little message for the
// user. We give a little feedback
        Toast.makeText(MainActivity.this,
                "Reset user name and password",
                Toast.LENGTH_LONG).show();
        createNotification();
    }

    public void createNotification() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
// prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Hello Preferences")
                .setContentText("Successfully reset user Credentials")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(activity)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0,notification);
        System.out.println("Notification");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
// We have only one menu option
            case R.id.action_settings:
// Launch Preference activity
                Intent i = new Intent(MainActivity.this, Preferences.class);
                startActivity(i);
// A toast is a view containing a quick little message for the user. This is shown at the bottom of the screen
                Toast.makeText(MainActivity.this, "Here you can store your user credentials.", Toast.LENGTH_LONG).show();
                Log.i("Main", "sent an intent to the Preference class!");
                break;
        }
        return true;
    }
/*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
             // Register the channel with the system; you can't change the importance
             // or other notification behaviors after this
             NotificationManager notificationManager = getSystemService(NotificationManager.class);
             notificationManager.createNotificationChannel(channel);
             }
        }
*/
    public void exitApplication (View v){
        finishAndRemoveTask();
    }


    /* Questions
    * 1: The preference class is called when the intent is called in onOptionsItemSelected(MenuItem item)
    *
    * 2:
    * 3: Implement exit function: see exitApplication()
    * 4: Use a fragment for the menu and the preferences window where you put your credentials
    * 5: OK
    * 6: Any app where you make persistent choices.
    *    For example a news app, where the app keeps track of which newsarticles have been read.
    *
    *
    *
    *
    *
    *
    * */
}
