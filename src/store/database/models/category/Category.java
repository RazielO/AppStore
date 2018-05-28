package store.database.models.category;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Model class with an empty constructor and one with params, getters and setters
 */
public class Category
{
    private Integer id;
    private String name;
    private Image logo;
    private File file;

    public Category()
    {
    }

    public Category(Integer id, String name, Image logo)
    {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public Image getLogo()
    {
        return logo;
    }

    public void setLogo(Image logo)
    {
        this.logo = logo;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }
}
