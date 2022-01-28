package io.daobab.test.generator;

import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.test.dao.table.Film;

import java.math.BigDecimal;
import java.util.*;

public class EntityBufferGenerator {

    private static final int size = 1000000;
    private final List<String> rates = Arrays.asList("perfect", "very good", "good", "medium", "poor", "disaster");

    public Entities<Film> getFilms(){
        List<Film> filmList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Film film = new Film();
            film.setFilmId(i);
            film.setDescription("sss");
            film.setRating(randomString(rates));
            film.setRentalRate(randomBigDecimal(new BigDecimal(10), new BigDecimal(20)));
            film.setLength(randomInt(60, 120));
            filmList.add(film);
        }
        Collections.shuffle(filmList);

        System.out.println("wygenewano filmow " + filmList.size());
        EntityList<Film> rv = new EntityList<>(filmList, Film.class);
        rv.calculateIndexes();
        return rv;
    }


    public Entities<Film> getFilms2() {
        List<Film> filmList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Film film = new Film();
            film.setFilmId(i);
            film.setDescription("sss");
            film.setRating(randomString(rates));
            film.setRentalRate(randomBigDecimal(new BigDecimal(10), new BigDecimal(20)));
            film.setLength(randomInt(60, 120));
            filmList.add(film);
        }
//        Collections.shuffle(filmList);

        System.out.println("wygenewano filmow " + filmList.size());
        EntityList<Film> rv = new EntityList<>(filmList, Film.class);
        rv.calculateIndexes();
        return rv;
    }


    private String randomString(List<String> stringList) {
        int length = stringList.size();
        Random rnd = new Random();
        return stringList.get(rnd.nextInt(length));
    }

    private Integer randomInt(int from,int to){
        int length=to-from;
        Random rnd=new Random();
        return rnd.nextInt(length)+from;
    }

    private BigDecimal randomBigDecimal(BigDecimal from, BigDecimal to){
        double length=from.doubleValue()-from.doubleValue();
        Random rnd=new Random();
        return new BigDecimal( ((from.doubleValue() + (to.doubleValue() - from.doubleValue())) * rnd.nextDouble()));
    }

    private Date randomDate(Date from, Date to){
        Long length=to.getTime()-from.getTime();
        Random rnd=new Random();
        return new Date(rnd.nextInt(length.intValue())+from.getTime());
    }
}
