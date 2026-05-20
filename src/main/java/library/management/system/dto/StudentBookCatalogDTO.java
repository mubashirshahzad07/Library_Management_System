package library.management.system.dto;

public class StudentBookCatalogDTO {

    private String author;
    private String title;
    private String category;
    private String availability;

    public StudentBookCatalogDTO(String author, String title, String category, int availableCopies) {
        this.author = author;
        this.title = title;
        this.category = category;

        if (availableCopies > 0) {
            this.availability = "Available";
        } else {
            this.availability = "Unavailable";
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAvailability() {
        return availability;
    }
}