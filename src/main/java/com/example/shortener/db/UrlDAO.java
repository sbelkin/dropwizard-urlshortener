package com.example.shortener.db;

import com.example.shortener.core.Url;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by sbelkin on 1/23/2016.
 */
public class UrlDAO extends AbstractDAO<Url> {

    public UrlDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Url> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Url create(Url url) {
        return persist(url);
    }

    public List<Url> findAll() {
        return list(namedQuery("Url.findAll"));
    }
}
