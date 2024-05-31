package org.daniclo.mixstarter.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class UserLikesPK implements Serializable {
    private long userKey;
    private long likedKey;

    public UserLikesPK(
            Long userKey,
            Long likedKey) {
        this.userKey = userKey;
        this.likedKey = likedKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikesPK that = (UserLikesPK) o;
        return userKey == that.userKey && likedKey == that.likedKey;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userKey, likedKey);
    }
}