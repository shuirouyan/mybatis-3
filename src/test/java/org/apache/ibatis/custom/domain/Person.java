package org.apache.ibatis.custom.domain;

import java.util.Date;

/**
 * @author ck163
 * @date 2022-04-06 13:45:50
 */
public class Person {
    private Long id;
    private String name;
    private String nickName;
    private Date createTime;
    private Integer age;
    private String phone;

    public Person() {
    }

    public Person(Long id, String name, String nickName, Date createTime, Integer age, String phone) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.createTime = createTime;
        this.age = age;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createTime=" + createTime +
                ", age=" + age +
                ", phone=" + phone +
                '}';
    }
}
