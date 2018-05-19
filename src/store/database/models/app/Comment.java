package store.database.models.app;

import java.sql.Date;

public class Comment
{
    private String comment, username;
    private Double rating;
    private Date date;
    private Long idUser, idApp;

    public Comment()
    {
    }

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

    public Long getIdUser()
    {
        return idUser;
    }

    public void setIdUser(Long idUser)
    {
        this.idUser = idUser;
    }

    public Long getIdApp()
    {
        return idApp;
    }

    public void setIdApp(Long idApp)
    {
        this.idApp = idApp;
    }
}
