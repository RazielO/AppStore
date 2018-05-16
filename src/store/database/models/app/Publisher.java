package store.database.models.app;

import javafx.scene.image.Image;

public class Publisher
{
    private Long id;
    private String name, description;
    private Image logo;

    public Publisher(Long id, String name, String description, Image logo)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Image getLogo()
    {
        return logo;
    }

    public void setLogo(Image logo)
    {
        this.logo = logo;
    }
}
