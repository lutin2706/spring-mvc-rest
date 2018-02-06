package be.superteam.springmvcrest.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BookDto {

    public int id;

    @NotNull
    @Size(min=1)
    public String title;
    public String summary;

    public BookDto() {
    }

    public BookDto(int id, String title, String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }
}
