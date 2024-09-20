package entities.business.genre;


import entities.business.Film.Film;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Genre name cannot be blank")
    @Column(unique = true, nullable = false)
    @Size(max = 100, message = "Genre name should not exceed 100 characters")
    private String name;

    @ManyToMany(mappedBy = "genresl", fetch = FetchType.EAGER)
    private Set<Film> films = new HashSet<>();




    public Genre(Object o, String genreName) {
        // Empty constructor
    }

    public Genre orElseGet(Object o) {
        return this;
    }
}