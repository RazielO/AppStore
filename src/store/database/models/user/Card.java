package store.database.models.user;

import java.sql.Date;

/**
 * Model class with an empty constructor and one with params, getters and setters
 */
public class Card
{
    private String number, name, lastName;
    private Integer cvv;
    private Date expirationDate;

    public Card()
    {
    }

    public Card(String number, String name, String lastName, Integer cvv, Date expirationDate)
    {
        this.number = number;
        this.name = name;
        this.lastName = lastName;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Integer getCvv()
    {
        return cvv;
    }

    public void setCvv(Integer cvv)
    {
        this.cvv = cvv;
    }

    public Date getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate)
    {
        this.expirationDate = expirationDate;
    }
}
