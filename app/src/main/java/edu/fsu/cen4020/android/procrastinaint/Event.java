package edu.fsu.cen4020.android.procrastinaint;

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
