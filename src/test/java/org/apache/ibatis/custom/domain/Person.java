package org.apache.ibatis.custom.domain;

import org.apache.ibatis.type.Alias;

import java.util.Date;
import java.util.List;

/**
 * @author ck163
 * @date 2022-04-06 13:45:50
 */
@Alias("person")
public class Person {
    private Integer id;
    private String name;
    private String nickName;
    private Date createTime;
    private Integer age;
    private String phone;
    private List<Book> isbnBook;

    public Person() {
    }

    public Person(Integer id, String name, String nickName, Date createTime, Integer age, String phone, List<Book> isbnBook) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.createTime = createTime;
        this.age = age;
        this.phone = phone;
        this.isbnBook = isbnBook;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getIsbnBook() {
        return isbnBook;
    }

    public void setIsbnBook(List<Book> isbnBook) {
        this.isbnBook = isbnBook;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createTime=" + createTime +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", isbnBook=" + isbnBook +
                '}';
    }
}
