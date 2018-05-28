package store.database.models.app;

import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

/**
 * Model class with an empty constructor and one with params, getters and setters
 */
public class App
{
    private Long id, downloads;
    private String name, description, version, features, compatibility, category, size, publisher;
    private Double rating, price;
    private Image logo;
    private List<Screenshot> screenshots;
    private List<Language> languages;
    private List<Comment> comments;
    private List<File> screenshotsFiles;
    private File logoFile;
    private boolean featured;

    public App()
    {

    }

    public App(Long id, Image logo, String name, String publisher, Double price, Double rating)
    {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.publisher = publisher;
        this.price = price;
        this.rating = rating;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getDownloads()
    {
        return downloads;
    }

    public void setDownloads(Long downloads)
    {
        this.downloads = downloads;
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

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getFeatures()
    {
        return features;
    }

    public void setFeatures(String features)
    {
        this.features = features;
    }

    public String getCompatibility()
    {
        return compatibility;
    }

    public void setCompatibility(String compatibility)
    {
        this.compatibility = compatibility;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Image getLogo()
    {
        return logo;
    }

    public void setLogo(Image logo)
    {
        this.logo = logo;
    }

    public List<Screenshot> getScreenshots()
    {
        return screenshots;
    }

    public void setScreenshots(List<Screenshot> screenshots)
    {
        this.screenshots = screenshots;
    }

    public List<Language> getLanguages()
    {
        return languages;
    }

    public void setLanguages(List<Language> languages)
    {
        this.languages = languages;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public List<File> getScreenshotsFiles()
    {
        return screenshotsFiles;
    }

    public void setScreenshotsFiles(List<File> screenshotsFiles)
    {
        this.screenshotsFiles = screenshotsFiles;
    }

    public File getLogoFile()
    {
        return logoFile;
    }

    public void setLogoFile(File logoFile)
    {
        this.logoFile = logoFile;
    }

    public boolean isFeatured()
    {
        return featured;
    }

    public void setFeatured(boolean featured)
    {
        this.featured = featured;
    }
}