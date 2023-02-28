package javase25.entity;

public class Student {

    String name;
    private final int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String yes(String str) {
        System.out.println(name + " " + age);
        return name;
    }
    public String test(String... str) {
        System.out.println(name + " " + age);
        return name;
    }
    private void pyes() {
        System.out.println(name + " " + age);
    }

}
