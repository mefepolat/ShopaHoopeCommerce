package com.shopahoop.admin.user;

import org.springframework.data.repository.CrudRepository;

import com.shopahoop.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
