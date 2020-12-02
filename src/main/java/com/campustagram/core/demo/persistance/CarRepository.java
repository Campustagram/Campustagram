package com.campustagram.core.demo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campustagram.core.demo.model.Car;

/**
 * created on 2018/09/28
 * 
 * @author Ahmet ŞEN
 *
 */
public interface CarRepository extends JpaRepository<Car, Long> {

}
