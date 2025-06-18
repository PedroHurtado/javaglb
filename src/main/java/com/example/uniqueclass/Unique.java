package com.example.uniqueclass;


import java.util.UUID;

public class Unique {
    private final UUID id;
    public Unique(UUID id){
        this.id =id;
    }
    UUID getId(){
        return id;
    }
    @Override
    public int hashCode() {
        
        return id.hashCode();

        //return Objects.hash(id);


        /*final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;*/
    }
    @Override
    public boolean equals(Object obj) {
        //https://openjdk.org/jeps/394
        if(obj instanceof Unique e){
            return e.id.equals(id);
        }
        return false;

        /*if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Unique other = (Unique) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;*/
    }
}
