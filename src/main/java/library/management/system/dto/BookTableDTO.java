package library.management.system.dto;

public class BookTableDTO {

    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private String availableDisplay;

    public BookTableDTO(
            int bookId,
            String isbn,
            String title,
            String author,
            String category,
            int totalCopies,
            boolean isActive,
            int availableCopies
    ) {

        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;

        if (!isActive) {
            this.availableDisplay = "Unavailable";

        } else {
            this.availableDisplay = String.valueOf(availableCopies);
        }
    }

    public int getBookId() {
        return bookId;
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

    public String getCategory() {
        return category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public String getAvailableDisplay() {
        return availableDisplay;
    }
}