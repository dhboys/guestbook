package org.dongho.guestbook.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Guestbook extends BaseEntity{

    @Id
    // 자동으로 만들어지는 값이다 -> GenerationType.IDENTITY => DB에 맞게 만들어준다(ex.mysql = AI)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    private String title;
    private String content;
    private String writer;

    // setter 의 역할
    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }
}
