package store.database.models.app;

import java.sql.Date;

public class Comment
{
    private String comment, username;
    private Double rating;
    private Date date;

    public Comment(String comment, String username, Double rating, Date date)
    {
        this.comment = comment;
        this.username = username;
        this.rating = rating;
        this.date = date;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
