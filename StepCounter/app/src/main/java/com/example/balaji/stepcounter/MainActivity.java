package com.example.balaji.stepcounter;

        import android.app.PendingIntent;
        import android.content.Intent;
        import android.location.Location;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.app.Activity;
        import android.content.Context;
        import android.hardware.*;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.net.Uri;
        import android.widget.Toast;
        import static android.R.attr.value;
        import static android.R.attr.x;
        import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
        import static java.lang.Float.floatToIntBits;
        import static java.lang.Float.parseFloat;

        import android.widget.Toast;
        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.Menu;
        import android.view.View;
        import android.widget.EditText;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.OutputStreamWriter;
        import java.io.PrintWriter;
        import java.util.Calendar;


public class MainActivity extends Activity implements SensorEventListener {


    private TextView textView, textView_countdisplay;

    private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;
    EditText ename, emobile, eweight, eheight;
    String e_name = " ", e_mobile = " ",e_weight = " ", e_height=" ";
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    int value1 = 0, value2=0, final_val=0;
    double calories_burnt=0;
    double e_weight1=0;

    double stride=0.000473485, distance_traversed=0;
    Button stop_btn;
    Location locationA = new Location("Start");
    Location locationB = new Location("End");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ename = (EditText) findViewById(R.id.name);
        emobile = (EditText) findViewById(R.id.mobile);
        eweight = (EditText) findViewById(R.id.weight);
        eheight = (EditText) findViewById(R.id.height);
        textView = (TextView) findViewById(R.id.count);

        textView_countdisplay = (TextView) findViewById(R.id.count);

        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }


    public void sendmsg(String no, String msg){
        SmsManager sms = SmsManager.getDefault();
        PendingIntent sentPI;
        String SENT = "SMS_SENT";

        sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);

        sms.sendTextMessage(no, null, msg, sentPI, null);
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        String miles_conv = " ", cal_conv = " ";

        if (values.length > 0) {
            // value1 = value = (int) values[0];
            value1 = (int) values[0];
            final_val = value1-value2-1;
        }



        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            double e_weight1 = 0, e_height1=0;
            e_weight = eweight.getText().toString();
            e_height = eweight.getText().toString();
            try{
                e_weight1 = Float.valueOf(eweight.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try{
                e_height1 = Float.valueOf(eheight.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            final_val = value1-value2-1;
            float calorie_permile   = (float) (0.57*e_weight1);
            double strip = e_height1 * 0.415;
            double steps_in_mile = 160934.4/strip;
            double conversion_factor = final_val/steps_in_mile;
            //float conversion_factor = calorie_permile/final_val;
            calories_burnt    = final_val*conversion_factor;
            distance_traversed = final_val*stride;
            miles_conv  = String.format("%.3f", distance_traversed);
            cal_conv  = String.format("%.3f", calories_burnt);
            String string4 = "Step Count : " + final_val + "\nStep Count : " + final_val +
                    "\nCalories Burnt : " + cal_conv +
                    "\nDistance Traversed : " + miles_conv + "miles";
            textView.setText(string4);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            double e_weight1 = 0, e_height1=0;
            e_weight = eweight.getText().toString();
            e_height = eweight.getText().toString();
            try{
                e_weight1 = Float.valueOf(eweight.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try{
                e_height1 = Float.valueOf(eheight.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            final_val = value1-value2-1;
            float calorie_permile   = (float) (0.57*e_weight1);
            double strip = e_height1 * 0.415;
            double steps_in_mile = 160934.4/strip;
            double conversion_factor = final_val/steps_in_mile;
            //float conversion_factor = calorie_permile/final_val;
            calories_burnt    = final_val*conversion_factor;
            distance_traversed = final_val*stride;
            miles_conv  = String.format("%.3f", distance_traversed);
            cal_conv  = String.format("%.3f", calories_burnt);
            String string4 = "Step Count : " + final_val + "\nStep Count : " + final_val +
                    "\nCalories Burnt : " + cal_conv +
                    "\nDistance Traversed : " + miles_conv + "miles";
            textView.setText(string4);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_FASTEST);

    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    public void start(View v)
    {
        e_name = ename.getText().toString();
        double latitudeA = locationA.getLatitude();
        double longitudeA = locationA.getLongitude();
        locationA.setLatitude(latitudeA);
        locationA.setLongitude(longitudeA);

        final_val = value1 - value2-1;
        //write_file(final_val);
        value2 = value1;

//        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//        String string  = date;
        String string = "\n" + "Hi..." + e_name;
        string += "\n" + "Initializing sensor.....Start Walking...!!!";
//        textView_countdisplay.setText(string);
        textView_countdisplay.setText(string);

    }

    public void stop1(View v) {

        double distance = 0;
        e_name   = ename.getText().toString();
        e_mobile = emobile.getText().toString();
        e_weight = eweight.getText().toString();
        e_height = eweight.getText().toString();
        double conversion_factor=0;
        //e_weight = Float.parseFloat();
        String tmp = e_weight;
        double e_weight1 = 0, e_height1=0;
        try{
            e_weight1 = Float.valueOf(eweight.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try{
            e_height1 = Float.valueOf(eweight.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // double e_weight1 = Double.parseDouble(tmp);
        Calendar cal = Calendar.getInstance();

        stop_btn = (Button) findViewById(R.id.stop);
        final_val = value1 - value2;

        //code for distance
        double latitudeB = locationB.getLatitude();
        double longitudeB = locationB.getLongitude();
        locationB.setLatitude(latitudeB);
        locationB.setLongitude(longitudeB);

        distance = (locationA.distanceTo(locationB)) * 1000;

        String dispsteps = "Steps - " + Integer.toString(final_val);
        String distancedisp = "Distance - " + distance;

        // textView_countdisplay.setText(String.valueOf(final_val));
        float calorie_permile   = (float) (0.57*e_weight1);
        if(final_val==0) {

            String string = " ";
            conversion_factor = 0;//calorie_permile / final_val;
            calories_burnt    = final_val*conversion_factor;

            dispsteps = Integer.toString(final_val);
            String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            String string9  = date;
            string9 += "\n" + "Hi..." + e_name;
            string9 += "\n" + "Your Step Count........" + dispsteps;
            string9 += "\n" + "Calories Burnt........." + calories_burnt;
            string9 += "\n" + "Distance Traversed....." + distance_traversed;
            string9 += "\n\n" + "Start Walking....";

            textView_countdisplay.setText(string9);
        }else{
            conversion_factor = calorie_permile / final_val;
            calories_burnt    = final_val*conversion_factor;
            distance_traversed = final_val*stride;





            float calorie_permile1   = (float) (0.57*e_weight1);
            double strip = e_height1 * 0.415;
            double steps_in_mile = 160934.4/strip;
            conversion_factor = final_val/steps_in_mile;
            //float conversion_factor = calorie_permile/final_val;
            calories_burnt    = final_val*conversion_factor;
            distance_traversed = final_val*stride;
            String miles_conv  = String.format("%.3f", distance_traversed);
            String cal_conv  = String.format("%.3f", calories_burnt);
            String string4 = "Step Count : " + final_val + "\nStep Count : " + final_val +
                    "\nCalories Burnt : " + cal_conv +
                    "\nDistance Traversed : " + miles_conv + "miles";

            //float distance_traversed = distance traversed;
            write_file(final_val,cal_conv,miles_conv);
            String phno = e_mobile;


            //For Mobile
            String string10 = " ";
            string10 += "\n" + "Hi..." + e_name;
            string10 += "\n" + "Your Step Count........" + final_val;
            string10 += "\n" + "Calories Burnt........." + cal_conv;
            string10+= "\n" + "Distance Traversed....." + miles_conv;
            sendmsg(phno, string10);
        }

        calories_burnt=0;
        distance_traversed=0;


    }


    public void write_file(int steps, String calories_burnt, String distance_traversed)
    {
        String dispsteps = Integer.toString(steps);
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String string  = date;
        string += "\n" + "Hi..." + e_name;
        string += "\n" + "Your Step Count........" + dispsteps;
        string += "\n" + "Calories Burnt........." + calories_burnt;
        string += "\n" + "Distance Traversed....." + distance_traversed;
        //Code for saving data in the phone memorry

        textView_countdisplay.setText(string);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }else {
            //Write Data to SD Card
            try {
                File myFile = new File("/sdcard/mysdfile.txt");
                myFile.createNewFile();



               /* File myFile = new File("/sdcard/mysdfile.txt");
                myFile.createNewFile();*/


                if(myFile.exists()==false){
                    System.out.println("New File created");
                    myFile.createNewFile();
                }
                PrintWriter out = new PrintWriter(new FileWriter(myFile, true));
                out.append(String.valueOf(string) + "\n\n");
                out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }

            value2 = value1;
                /*
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append(String.valueOf(string));
                myOutWriter.close();
                fOut.close();
                Toast.makeText(getBaseContext(),
                        "Done writing SD 'mysdfile.txt'",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            */}
    }

    public void reset(View v)
    {

        //value1 = 0;
        //value2 = 0;
        e_name = ename.getText().toString();
        value2 = value1;
        calories_burnt=0;
        distance_traversed=0;
        int final_val = value1-value2;
//        textView_countdisplay.setText(String.valueOf(final_val));
        //textView_countdisplay.setText("0");

        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String string  = date;
        string += "\n" + "Hi..." + e_name;
        string += "\n" + "Step Count Initialized to.....\n" + final_val;
        //Code for saving data in the phone memorry

        textView.setText(string);
//        textView_countdisplay.setText(string);
    }
}
