package entity;

import lombok.Data;

/**
 * hello.
 *
 * @author ViJay
 * @date 2020-12-24
 */
@Data
public class Hello {
    private Integer id;
    private String name;
    private Integer age;

    public static Hello builder(Integer id) {
        Hello hello = new Hello();
        hello.setId(id);
        hello.setName("hello");
        hello.setAge(20);
        return hello;
    }

    public static Hello builder(Integer id, String name) {
        Hello hello = new Hello();
        hello.setId(id);
        hello.setName(name);
        hello.setAge(20);
        return hello;
    }
}
