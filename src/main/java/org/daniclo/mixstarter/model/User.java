package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String username;

    @ToString.Exclude
    @Column(name = "contraseña", nullable = false)
    private String password;

    @Column(name = "likesPublicos", nullable = false)
    private boolean publicLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "follows", cascade = CascadeType.MERGE)
    private List<Followers> followers;

    @ToString.Exclude
    @OneToMany(mappedBy = "followed", cascade = CascadeType.MERGE)
    private List<Followers> followed;
}