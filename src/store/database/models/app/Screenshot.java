package store.database.models.app;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Model class with an empty constructor and one with params, getters and setters
 */
public class Screenshot
{
    private Long id;
    private Image screenshot;
    private File file;

    public Screenshot()
    {
    }

    public Screenshot(Long id, Image screenshot)
    {
        this.id = id;
        this.screenshot = screenshot;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Image getScreenshot()
    {
        return screenshot;
    }

    public void setScreenshot(Image screenshot)
    {
        this.screenshot = screenshot;
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
