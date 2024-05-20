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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id_album")
    private Album album;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "tag_id_tag", nullable = false)
    private Tag tag;

    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Post post;

}
