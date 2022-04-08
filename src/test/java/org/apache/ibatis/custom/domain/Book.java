package org.apache.ibatis.custom.domain;

import java.util.Date;

/**
 * @author ck163
 * @date 2022-04-07 13:53:45
 */
public class Book {
    private Long id;
    private String isbn;
    private String bookName;
    private String bookDescription;
    private Integer bookTotal;
    private Date createTime;
    private Date updateTime;

    public Book() {
    }

    public Book(Long id, String isbn, String bookName, String bookDescription, Integer bookTotal, Date createTime, Date updateTime) {
        this.id = id;
        this.isbn = isbn;
        this.bookName = bookName;
        this.bookDescription = bookDescription;
        this.bookTotal = bookTotal;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public Integer getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(Integer bookTotal) {
        this.bookTotal = bookTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", bookTotal=" + bookTotal +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
