package logos.jpabasic_example.domain;

import javax.persistence.Entity;

@Entity
public class Book extends Item {
    private String author;
    private Integer isbn;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
