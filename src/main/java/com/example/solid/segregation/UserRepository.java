package com.example.solid.segregation;

public class UserRepository implements Get<User,Integer> {

    @Override
    public User get(Integer id) {
        return new User();
    }
    
}
