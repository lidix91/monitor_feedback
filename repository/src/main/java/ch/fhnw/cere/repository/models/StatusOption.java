package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class StatusOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int order;
    private boolean userSpecific;

    public StatusOption() {
    }

    @Override
    public String toString() {
        return String.format(
                "StatusOption[id=%d, name='%s', order='%d', userSpecific='%b']",
                id, name, order, userSpecific);
    }
}
