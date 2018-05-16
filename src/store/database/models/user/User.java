package store.database.models.user;

import javafx.scene.image.Image;

import java.io.File;
import java.sql.Date;

public class User
{
    private Long id = null;
    private String name, lastName, address, phone, email, cardNumber, password, username;
    private Boolean admin;
    private Date birthday;
    private Image picture;
    private File imageFile;

    public User()
    {
    }

    public User(Long id, String name, String lastName, String address, String phone, String email, String cardNumber, String password, String username, Boolean admin, Date birthday, Image picture)
    {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.cardNumber = cardNumber;
        this.password = password;
        this.username = username;
        this.admin = admin;
        this.birthday = birthday;
        this.picture = picture;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public File getImageFile()
    {
        return imageFile;
    }

    public void setImageFile(File imageFile)
    {
        this.imageFile = imageFile;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCardNumber()
    {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    public Boolean getAdmin()
    {
        return admin;
    }

    public void setAdmin(Boolean admin)
    {
        this.admin = admin;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Image getPicture()
    {
        return picture;
    }

    public void setPicture(Image picture)
    {
        this.picture = picture;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
