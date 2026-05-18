package g10docmansys;

public class Document {

    private String id;
    private String title;
    private String author;
    private String category;
    private String description;
    private String status;
    private double version;
    private String owner;

    // Constructor
    public Document(String id, String title, String author, String category,
                    String description, String status, double version, String owner) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.status = status;
        this.version = version;
        this.owner = owner;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public double getVersion() {
        return version;
    }

    public String getOwner() {
        return owner;
    }

    // Setters (only editable fields)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    // toString() for displaying in console
    @Override
    public String toString() {
        return "ID: " + id +
               "\nTitle: " + title +
               "\nAuthor: " + author +
               "\nCategory: " + category +
               "\nDescription: " + description +
               "\nStatus: " + status +
               "\nVersion: " + version +
               "\nOwner: " + owner +
               "\n-------------------------";
    }

    // Convert object to file string (for saving)
    public String toFileString() {
        return id + "|" +
               title + "|" +
               author + "|" +
               category + "|" +
               description + "|" +
               status + "|" +
               version + "|" +
               owner;
    }
}