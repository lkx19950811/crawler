package com.leno.crawler.service;

import com.leno.crawler.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author leon
 * @date 2018-06-21 16:54
 * @desc 解析Movie
 */
@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
}
