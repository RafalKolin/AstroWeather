package astroLab.myastro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView sunrise;
    private TextView sunset;
    private TextView moonRise;
    private TextView moonSet;
    private TextView civilDawn;
    private TextView civilTwilight;
    private TextView sunriseAzimuth;
    private TextView sunsetAzimuth;
    private TextView nextFullMoon;
    private TextView moonPhase;
    private TextView newMoon;
    private TextView synodicDay;

    private int hour;
    private int minute;
    private int second;
    private String timeStr;
    boolean run = true;
    Handler handler = new Handler();


    public String newLon;
    public String newLat;



    private double lonVal;
    private double latVal;

    private TextView localDeviceTime;
    private AstroCalculator.Location location1 = new AstroCalculator.Location(latVal, lonVal);
    AstroDateTime astroDateTime = new AstroDateTime();
    LocalDateTime localDateTime = LocalDateTime.now();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent intent1 = getIntent();
        newLon =  intent1.getStringExtra("lonVal");
        newLat =  intent1.getStringExtra("latVal");
        Log.i("lonVal1", newLon);
        Log.i("latVal1", newLat);
        lonVal = Double.parseDouble(String.valueOf(newLon));
        latVal = Double.parseDouble(String.valueOf(newLat));


        localDeviceTime = findViewById(R.id.localDeviceTime);


        displayMyAstro();
        currentTime();
  }

    public AstroDateTime setAstroDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        this.astroDateTime = new AstroDateTime(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond(),
                0,
                true);

        return astroDateTime;
    }



    public void displayMyAstro() {
        DecimalFormat decimalFormatAzimuth = new DecimalFormat("#.##");

        AstroCalculator myAstroResponse = new AstroCalculator(setAstroDateTime(this.localDateTime), this.location1);
        sunset = findViewById(R.id.sunset);
        sunset.append("\n" + myAstroResponse.getSunInfo().getSunset().toString().substring(10,19));

        sunsetAzimuth = findViewById(R.id.sunsetAzimuth);
        sunsetAzimuth.append("\n" + (decimalFormatAzimuth.format(myAstroResponse.getSunInfo().getAzimuthSet())));

        sunrise = findViewById(R.id.sunrise);
        sunrise.append("\n" + myAstroResponse.getSunInfo().getSunrise().toString().substring(10,19));

        sunriseAzimuth = findViewById(R.id.sunriseAzimuth);
        sunriseAzimuth.append("\n" + (decimalFormatAzimuth.format(myAstroResponse.getSunInfo().getAzimuthRise())));

        civilDawn = findViewById(R.id.civilDawn);
        civilDawn.append("\n" + myAstroResponse.getSunInfo().getTwilightMorning().toString().substring(10, 19));

        civilTwilight = findViewById(R.id.civilTwilight);
        civilTwilight.append("\n" + (myAstroResponse.getSunInfo().getTwilightEvening().toString().substring(10,19)));

        moonRise = findViewById(R.id.moonrise);
        moonRise.append("\n" + myAstroResponse.getMoonInfo().getMoonrise().toString().substring(10,19));

        moonSet = findViewById(R.id.moonset);
        moonSet.append("\n" + myAstroResponse.getMoonInfo().getMoonset().toString().substring(10,19).trim());

        nextFullMoon = findViewById(R.id.nextFullmoon);
        nextFullMoon.append("\n" + myAstroResponse.getMoonInfo().getNextFullMoon().toString().substring(0,16));

        moonPhase = findViewById(R.id.moonPhase);
        moonPhase.append("\n" + Math.round((myAstroResponse.getMoonInfo().getIllumination())*100) + "%");

        newMoon = findViewById(R.id.newMoon);
        newMoon.append("\n" + myAstroResponse.getMoonInfo().getNextNewMoon().toString().substring(0,16));

        synodicDay = findViewById(R.id.synodicDay);
        synodicDay.append("\n" + (int)myAstroResponse.getMoonInfo().getAge());


    }
    public void currentTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(500);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Calendar c = Calendar.getInstance();
                                hour = c.get(Calendar.HOUR_OF_DAY);
                                if (hour > 12){
                                    hour = hour - 12;
                                }
                                minute = c.get(Calendar.MINUTE);
                                second = c.get(Calendar.SECOND);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

                                Date date = c.getTime();
                                timeStr = simpleDateFormat.format(date);
                                localDeviceTime.setText(timeStr);
                            }
                        });
                    }
                    catch (Exception ex) {
                        localDeviceTime.setText(ex.getMessage());
                    }
                }
            }
        }).start();
    }




}
