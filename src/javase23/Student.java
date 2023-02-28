package javase23;

public class Student {

    String name;
    int age;

    /*public Student(String text) {

    }*/
    /*public Student() {
        System.out.println("我是无参对象");
    }*/
    /*private Student() {
        System.out.println("我是无参对象");
    }*/
    /*public Student(String text) {
        System.out.println(text);
    }
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }*/
    private Student(String text) {
        System.out.println(text);
    }
    private Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void yes() {
        //System.out.println("萨日朗");
        System.out.println(name + " " + age);
    }

}
