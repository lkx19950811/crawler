package com.leno.crawler.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述:
 *
 * @author Leo
 * @create 2017-12-17 下午 10:58
 */
@NoRepositoryBean
@Transactional
public interface Repository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

}
