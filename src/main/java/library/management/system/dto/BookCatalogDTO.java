package library.management.system.dto;

public class BookCatalogDTO {

    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private String status;

    public BookCatalogDTO(String isbn, String title, String author, int totalCopies, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;

        if (availableCopies > 0) {
            this.status = "Available";
        } else {
            this.status = "Unavailable";
        }
    }

    public String getIsbn() {
        return isbn;
    }
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getStatus() {
        return status;
    }
}