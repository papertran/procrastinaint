package edu.fsu.cen4020.android.procrastinaint;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Event {
    String Title;
    String Description;
    String RRULE;
    String Duration;
    Long DTSTART;
    Long DTEND;
    Long LAST_DATE;

    public Event(){

    }

    public Event(String title, String description, String RRULE, String duration, Long DTSTART, Long DTEND, Long LAST_DATE) {
        Title = title;
        Description = description;
        this.RRULE = RRULE;
        Duration = duration;
        this.DTSTART = DTSTART;
        this.DTEND = DTEND;
        this.LAST_DATE = LAST_DATE;
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

        return (this.getTitle().equals(p.getTitle()) && this.getDTSTART().equals(p.getDTSTART()));
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

    // Helper Functions
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
