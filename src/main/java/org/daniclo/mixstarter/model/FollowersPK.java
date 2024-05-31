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
public class FollowersPK implements Serializable {
    private long userFollows;
    private long userFollowed;

    public FollowersPK(long userFollows, long userFollowed) {
        this.userFollows = userFollows;
        this.userFollowed = userFollowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowersPK that = (FollowersPK) o;
        return userFollows == that.userFollows && userFollowed == that.userFollowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userFollows, userFollowed);
    }
}
