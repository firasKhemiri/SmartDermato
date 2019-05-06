package smartdermato.esprit.tn.smartdermato.Util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class util {
    private static String domaneName ;

    public static String getDomaneName() {
        return "http://"+"438d5969.ngrok.io";
    }


    public static void setDomaneName(String domaneName) {
        util.domaneName = domaneName;
    }




    public String convertDate(String strDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        Calendar c = Calendar.getInstance();

        Date d = dateFormat.parse(strDate);

        Date s = c.getTime();

        long diffInMs = ((s.getTime() - d.getTime()) / 1000) - 7200;


        Long days = diffInMs / (60 * 60 * 24);
        //diffInMs -= days * (60 * 60 * 24);
        // Long hours = diffInMs / (60 * 60);

        Long hours = ((60 - (-diffInMs / 60)) / 60);
        // diffInMs -= hours * (60 * 60);
        Long minutes = (60 - (-diffInMs / 60))-1;
        // diffInMs -= minutes *60;
        //Long seconds = -diffInMs / 60 /60;


        //  String temp = " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;


        if (days >= 1)
            // diff = TimeUnit.MILLISECONDS.toMinutes(diffInMs);
            return days + "j";
        else if (hours >= 1)
            //  diff = TimeUnit.MILLISECONDS.toHours(diffInMs);
            return hours + "h";
        else if (minutes >= 1)
            //  diff = TimeUnit.MILLISECONDS.toDays(diffInMs);
            return minutes + "m";
        else
            // diff = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            return "quelques instants";

//        return " j: "+days+ " / H: "+hours+ " / M: "+minutes+" / S: "+diffInMs;
    }



}
