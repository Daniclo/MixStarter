package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Album")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlbum", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @ToString.Exclude
    @OneToOne(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Post post;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "tag_idtag", nullable = false)
    private Tag tag;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Song> songs = new ArrayList<>();

}
