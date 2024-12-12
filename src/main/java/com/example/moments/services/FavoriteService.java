package com.example.moments.services;

import com.example.moments.entities.Favorite;
import com.example.moments.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public Optional<Favorite> getFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
