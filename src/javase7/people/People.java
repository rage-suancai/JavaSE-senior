package javase7.people;

import java.io.Serializable;

public class People implements Serializable {

    public String name;
    public transient String age;

    public People(String name) {
        this.name = name;
    }

}
