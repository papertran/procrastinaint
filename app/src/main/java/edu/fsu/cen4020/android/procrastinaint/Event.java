package edu.fsu.cen4020.android.procrastinaint;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class Event {


    private static final String TAG = Event.class.getCanonicalName();
    String Title;
    String Description;
    String RRULE;
    String Duration;
    Long DTSTART;
    Long DTEND;
    Long LAST_DATE;
    int write;
    boolean recurring;

    public Event(){

    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setWrite(int write) {
        this.write = write;
    }

    public Event(String title, String description, String RRULE, String duration, Long DTSTART, Long DTEND, Long LAST_DATE) {
        Title = title;
        Description = description;
        this.RRULE = RRULE;
        Duration = duration;
        this.DTSTART = DTSTART;
        this.DTEND = DTEND;
        this.LAST_DATE = LAST_DATE;
        if(this.RRULE == null){
            this.recurring = false;
        } else{
            this.recurring = true;
        }
    }

    public Boolean isEqual(Event event){
        return (this.getTitle() == event.getTitle() && this.getDTSTART() == event.getDTSTART());
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        Event p = (Event)o;

        return (this.getTitle().equals(p.getTitle()) );
    }


    @Override
    public int hashCode() {

        return this.getTitle().hashCode();
    }


    public String getEventStartDate(){
        if (this.getDTSTART() != 0 && this.getDTSTART() != null){
            return epochToDate(this.getDTSTART());

        }
        return null;
    }

    public String getEventStartTime(){
        if (this.getDTSTART() != 0 && this.getDTSTART() != null){
            return epochToTime(this.getDTSTART());
        }
        return null;
    }

    public String getEventEndDate(){
        if (this.getLAST_DATE() != 0 && this.getLAST_DATE() != null){
            return epochToDate(this.getLAST_DATE());
        }
        else{
            // Check DT for correct end time
            if(this.getDTEND() != 0 && this.getDTEND() == null)  {
                return epochToDate(this.getDTEND());
            }
            return null;
        }
    }

    public String getEventEndTime(){
        if (this.getDTEND() == null){
            Long newDuration = RFC2445ToMilliseconds(this.getDuration());
            return epochToTime(this.getDTSTART() + newDuration);
        } else if( this.getDTEND() != 0){
            return epochToTime(this.getDTEND());
        }
        else{
            try {
                Long newDuration = RFC2445ToMilliseconds(this.getDuration());
                return epochToTime(this.getDTSTART() + newDuration);
            }
            catch (Exception e){
                return null;
            }
        }
    }

    public ArrayList<Event> recurringToSingular(Long currentTime){
        ArrayList<Event> newEvents = new ArrayList<Event>();

        if(!isRecurring()){
            return null;
        }

        // Get list of days from that reoccur
        ArrayList<String> recurringDays = RRuleToDays(this.getRRULE());

//        Log.i(TAG, "recurringToSingular: rruel = " + this.getRRULE());
//        for(String item : recurringDays)
//        {
//            Log.i(TAG, "recurringToSingular: New Rule is = " + item);
//        }


        // Check if the event has passed already



        ArrayList<Long> distanceApart = new ArrayList<Long>();
        for(int i = 0; i < recurringDays.size(); i++){
            if(recurringDays.size() == 1){
                distanceApart.add(daysApart(recurringDays.get(i)));
            }
            else if( i == recurringDays.size() -1){
                distanceApart.add(daysApart(recurringDays.get(i),
                        recurringDays.get(0)));
            } else{
                distanceApart.add(daysApart(recurringDays.get(i),
                        recurringDays.get(i+1)));
            }
        }




        // newStartDate calulate from time difference betewen two date, then adding to DTStart
        Long newStartDate;

        int i = 0;

        if(currentTime > this.getDTSTART()){
            newStartDate =  this.getDTSTART() + ( ((currentTime - this.getDTSTART()) / 604800000)  * 604800000 );
            String weekDay = epochToWeekDay(newStartDate);

            i = recurringDays.indexOf(weekDay);
            while(newStartDate < currentTime){
                Log.i(TAG, "recurringToSingular: This is the Currently " + "\nTitle = " + this.getTitle() +
                        "\nCurrent date = " + epochToDate(newStartDate) + "\nday = " + epochToWeekDay(newStartDate));

                if(i == distanceApart.size()-1) {
                    newStartDate += distanceApart.get(i);
                    i = 0;
                } else{
                    newStartDate += distanceApart.get(i);
                    i++;
                }

            }

        } else {
            newStartDate = DTSTART;
        }

        currentTime = newStartDate;

//        Log.i(TAG, "recurringToSingular: GG \n Ttile =" + this.getTitle() + "\nCurrent Time = " + epochToDate(currentTime) + "\nDay = " + epochToWeekDay(currentTime));

        while(currentTime < this.getLAST_DATE()){
            Event event = new Event(this.getTitle(), this.getDescription(), null, null, currentTime, currentTime+ RFC2445ToMilliseconds(this.getDuration()), currentTime+ RFC2445ToMilliseconds(this.getDuration()));
            event.setWrite(1);
            newEvents.add(event);

            if(i == distanceApart.size()-1) {
                currentTime += distanceApart.get(i);
                i = 0;
            } else{
                currentTime += distanceApart.get(i);
                i++;
            }
        }

//
//        for(Event item : newEvents){
//            Log.i(TAG, " recurringToSingle NEW REPEATING EVENT" +
//                    "\nTitle =" + item.getTitle() +
//                    "\nDTStart = " + item.getEventStartDate() + " On " + item.epochToWeekDay(item.getDTSTART())+
//                    "\nEnd Date = " + item.getEventEndTime() + " On " + item.epochToWeekDay(item.getDTEND())+
//                    "\nDTStart = " + item.getEventStartTime() +
//                    "\nEnd Date = " + item.getEventEndTime());
//        }

        return newEvents;
    }



    // Helper Functions
    // https://stackoverflow.com/questions/43676183/convert-unix-time-to-week-day
    private String epochToWeekDay(Long epochSeconds){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        Date dateFormat = new java.util.Date(epochSeconds);
        return sdf.format(dateFormat );
    }
    private  String epochToDate(Long epocSeconds){
        Date updateDate = new Date(epocSeconds);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return format.format(updateDate);
    }

    //https://stackoverflow.com/questions/4142313/convert-timestamp-in-milliseconds-to-string-formatted-time-in-java
    private  String epochToTime(Long epocSeconds){
        Date date = new Date(epocSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return sdf.format(date);
    }

    private Long msToDays(Long seconds){
        return seconds / 86400000;
    }

    // Turns RRule to list of days
    private ArrayList<String> RRuleToDays(String rRule){

        int indexPoint = rRule.indexOf("BYDAY=");
        rRule = rRule.substring(indexPoint);
        if(rRule == null){
            rRule = "";
        }
        ArrayList<String> Days = new ArrayList<String>();
        if(rRule.contains("SU")){
            Days.add("Sunday");
        }
        if(rRule.contains("MO")){
            Days.add("Monday");
        }
        if(rRule.contains("TU")){
            Days.add("Tuesday");
        }
        if(rRule.contains("WE")){
            Days.add("Wednesday");
        }
        if(rRule.contains("TH")){
            Days.add("Thursday");
        }
        if(rRule.contains("FR")){
            Days.add("Friday");
        }
        if(rRule.contains("SA")){
            Days.add("Saturday");
        }
        return Days;
    }


    private Long daysApart(String day1){
         return 604800000L;
    }
    private Long daysApart(String day1, String day2){
        HashMap<String, Integer> dayToInt =new  HashMap<String, Integer>(){{
            put("Monday", 1);
            put("Tuesday", 2);
            put("Wednesday", 3);
            put("Thursday", 4);
            put("Friday", 5);
            put("Saturday", 6);
            put("Sunday", 7);
        }};

        if(dayToInt.containsKey(day1) && dayToInt.containsKey(day2)){
            Integer int1 = dayToInt.get(day2);
            Integer int2 = dayToInt.get(day1);
            Integer timeApart;
            // Subtract cause have to get to sunday first
            if(int1 < int2){
                timeApart = 7 - int2 + int1;
            } else{
                timeApart = int2 - int1;
            }

            if(timeApart < 0)
                timeApart *= -1;

            return 86400000L * timeApart;

        } else{
            Log.i(TAG, "daysApart: day1 = " + day1 +"\nday2 = " + day2);
            return null;
        }
    }

    private  long RFC2445ToMilliseconds(String str)
    {


        if(str == null || str.isEmpty())
            throw new IllegalArgumentException("Null or empty RFC string");

        int sign = 1;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        int len = str.length();
        int index = 0;
        char c;

        c = str.charAt(0);

        if (c == '-')
        {
            sign = -1;
            index++;
        }

        else if (c == '+')
            index++;

        if (len < index)
            return 0;

        c = str.charAt(index);

        if (c != 'P')
            throw new IllegalArgumentException("Duration.parse(str='" + str + "') expected 'P' at index="+ index);

        index++;
        c = str.charAt(index);
        if (c == 'T')
            index++;

        int n = 0;
        for (; index < len; index++)
        {
            c = str.charAt(index);

            if (c >= '0' && c <= '9')
            {
                n *= 10;
                n += ((int)(c-'0'));
            }

            else if (c == 'W')
            {
                weeks = n;
                n = 0;
            }

            else if (c == 'H')
            {
                hours = n;
                n = 0;
            }

            else if (c == 'M')
            {
                minutes = n;
                n = 0;
            }

            else if (c == 'S')
            {
                seconds = n;
                n = 0;
            }

            else if (c == 'D')
            {
                days = n;
                n = 0;
            }

            else if (c == 'T')
            {
            }
            else
                throw new IllegalArgumentException ("Duration.parse(str='" + str + "') unexpected char '" + c + "' at index=" + index);
        }

        long factor = 1000 * sign;
        long result = factor * ((7*24*60*60*weeks)
                + (24*60*60*days)
                + (60*60*hours)
                + (60*minutes)
                + seconds);

        return result;
    }

    // Setters and Getters
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRRULE() {
        return RRULE;
    }

    public void setRRULE(String RRULE) {
        this.RRULE = RRULE;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public Long getDTSTART() {
        return DTSTART;
    }

    public void setDTSTART(Long DTSTART) {
        this.DTSTART = DTSTART;
    }

    public Long getDTEND() {
        return DTEND;
    }

    public void setDTEND(Long DTEND) {
        this.DTEND = DTEND;
    }

    public Long getLAST_DATE() {
        return LAST_DATE;
    }

    public void setLAST_DATE(Long LAST_DATE) {
        this.LAST_DATE = LAST_DATE;
    }
}
