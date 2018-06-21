package com.leno.crawler.service;

import com.leno.crawler.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author leon
 * @date 2018-06-21 16:55
 * @desc 解析短评
 */
@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
}
