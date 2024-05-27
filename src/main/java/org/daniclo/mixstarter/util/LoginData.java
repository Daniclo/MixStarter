package org.daniclo.mixstarter.util;

import lombok.Getter;
import lombok.Setter;
import org.daniclo.mixstarter.model.User;

public class LoginData {
    @Setter
    @Getter
    private static User currentUser;
    private LoginData(){}
}
