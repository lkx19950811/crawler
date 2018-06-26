package com.leno.crawler.repository;

import com.leno.crawler.common.Repository;
import com.leno.crawler.entity.Movie;

import java.util.List;

/**
 * @author leon
 * @date 2018-06-21 17:17
 * @desc
 */
public interface MovieRepository extends Repository<Movie> {
    List<Movie> findByName(String name);
}
