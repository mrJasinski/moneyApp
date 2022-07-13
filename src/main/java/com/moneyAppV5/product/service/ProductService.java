package com.moneyAppV5.product.service;

import com.moneyAppV5.product.Genre;
import com.moneyAppV5.product.Unit;
import com.moneyAppV5.product.dto.GenreDTO;
import com.moneyAppV5.product.dto.UnitDTO;
import com.moneyAppV5.product.repository.GenreRepository;
import com.moneyAppV5.product.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService
{
    private final GenreRepository genreRepository;
    private final UnitRepository unitRepository;

    public ProductService(GenreRepository genreRepository, UnitRepository unitRepository) {
        this.genreRepository = genreRepository;
        this.unitRepository = unitRepository;
    }

    public List<GenreDTO> readAllGenresAsDto()
    {
        return this.genreRepository.findAll().stream().map(Genre::toDto).collect(Collectors.toList());
    }

    public void createGenre(GenreDTO current)
    {
        var result = current.toGenre();

        result.setHash(result.hashCode());

        this.genreRepository.save(result);
    }

    public boolean genreExistsInDatabase(String name)
    {
       return this.genreRepository.existsByName(name);
    }

    public List<UnitDTO> readAllUnitsAsDTO()
    {
        return this.unitRepository.findAll().stream().map(Unit::toDto).collect(Collectors.toList());
    }

    public List<Genre> readAllGenres()
    {
        return this.genreRepository.findAll();
    }

    public List<Unit> readAllUnits()
    {
        return this.unitRepository.findAll();
    }
}
