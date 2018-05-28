package store.database.models.app;

/**
 * Model class with an constructor, getters and setters
 */
public class Language
{
    private String id, name;

    public Language(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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
}
