package com.example.solid.segregation;

public class CustomerRepository  implements Repository<Customer,Integer>{

    @Override
        public Customer get(Integer id) {
            return new Customer();
        }

        @Override
        public void remove(Customer entity) {
        
        }

        @Override
        public void update(Customer id) {
        
        }

        @Override
        public void add(Customer entity) {
        
        }
    
}
