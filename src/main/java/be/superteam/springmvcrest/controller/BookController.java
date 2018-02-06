package be.superteam.springmvcrest.controller;

import be.superteam.springmvcrest.model.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @Controller
// ancienne méthode, avant que le RestController soit intégré à Spring
@RestController
public class BookController {

    private static List<BookDto> books = new ArrayList<>();

    static {
        books.add(new BookDto(1, "Agenda pour une vie Wouaw 2018", "L'agenda qui change la vie"));
        books.add(new BookDto(2, "Sors de ce corps, William !", "Un livre marrant sur la réincarnation"));
        books.add(new BookDto(3, "La bible", "Un livre pas marrant sur le paradis et l'enfer"));
    }

    @GetMapping("/books")
    public List<BookDto> getAllBooks() {
        return books;
    }

    @GetMapping("/books/{id}")
    public ResponseEntity getBook(@PathVariable("id") int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        BookDto result = books.stream().filter(b -> b.id == id).findFirst().orElse(null);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    // region POST - old versions

// ancienne méthode, avec un Controller traditionnel
//    @PostMapping("/books")
//    @ResponseStatus(HttpStatus.CREATED)
//    public @ResponseBody BookDto createBook(@RequestBody BookDto bookDto) {
//    public BookDto createBook(@RequestBody BookDto bookDto) {
//        bookDto.id = 123;
//        return bookDto;
//    }

    // Si je veux plus de contrôle sur la réponse, je peux la construire moi-même
//    @PostMapping("/books")
//    public ResponseEntity createBookResponse(@RequestBody BookDto bookDto) {
//
//        HashMap<String, String> errors = new HashMap<>();
//
//        if (bookDto.title == null || bookDto.title.isEmpty()) {
//            errors.put("title", "Title is required");
//            return ResponseEntity
//                    .unprocessableEntity()
//                    .body(errors);
////            return ResponseEntity
////                    .unprocessableEntity()
////                    .header("X-Errors", "Title is required")
////                    .build();
//        }
//
//        bookDto.id = 321;
//        return ResponseEntity
//                .created(URI.create("http://localhost:8080/books/321"))
//                .build(); // .body(bookDto);
//    }

    // endregion

    // Je peux aussi utiliser la validation Spring
    @PostMapping("/books")
    public ResponseEntity createBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
        }
        bookDto.id = books.size();
        books.add(bookDto);
        return ResponseEntity
                .created(URI.create("http://localhost:8080/books/321"))
                .build(); // .body(bookDto);
    }

    // region
    @PutMapping("/books/{id}")
    public ResponseEntity updateBook(@PathVariable("id") int id, @Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bookDto.id != id || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        BookDto result = books.stream().filter(b -> b.id == id).findFirst().orElse(null);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(bindingResult.getAllErrors());
        }

        books.set(id-1, bookDto);
        return  ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteBookById(@PathVariable("id") int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        BookDto result = books.stream().filter(b -> b.id == id).findFirst().orElse(null);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        books.remove(id-1);

        return ResponseEntity.ok().build();
    }
}
