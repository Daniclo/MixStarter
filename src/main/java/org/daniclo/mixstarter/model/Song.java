package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Cancion")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCancion", nullable = false)
    private Long id;

    @Column(name = "nombreCancion", nullable = false)
    private String name;

    @Column(name = "duracion", nullable = false)
    private Integer length;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "Album_idAlbum")
    private Album album;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "Tag_idTag", nullable = false)
    private Tag tag;

    @OneToOne(mappedBy = "song", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Post post;

}
