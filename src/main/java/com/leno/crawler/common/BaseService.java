package com.leno.crawler.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Allen on 31/03/2017.
 * coder.allen@hotmail.com
 */
public class BaseService<T> {
	
	protected Logger logger =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    JpaRepository<T, Long> jpaRepository;
	@Autowired
    JpaSpecificationExecutor<T> jpaSpecificationExecutor;
	
	public T get(long id){
    	return jpaRepository.getOne(id);
    }
	
	public Page<T> findAll(Pageable pageable){
    	return jpaRepository.findAll(pageable);
    }
	
	public List<T> findAll(){
    	return jpaRepository.findAll();
    }
    
	public Page<T> findAll(Specification<T> spec, Pageable pageable){
    	return jpaSpecificationExecutor.findAll(spec, pageable);
    }
	
	public List<T> findAll(Specification<T> spec){
    	return jpaSpecificationExecutor.findAll(spec);
    }
	
	@Transactional
    public void save(T entity){
    	jpaRepository.save(entity);
    }
    
	@Transactional
    public void delete(long id){
    	jpaRepository.deleteById(id);
    }
    
	@Transactional
    public void delete(T entity){
    	jpaRepository.delete(entity);
    }
    
}
