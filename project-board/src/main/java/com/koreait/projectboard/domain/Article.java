package com.koreait.projectboard.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)       // 자동증가,
    private Long id;
    @Setter @Column(nullable=false) private String title;
    @Setter @Column(nullable=false, length = 10000) private String content;
    @Setter private String hashtag;
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;
    @CreatedBy @Column(nullable = false, length = 100) private String createdBy;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;

    @ToString.Exclude       // 이거 뽑을때는 투스트링 제외하고 뽑아와라
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    protected Article(){}   // 빈 생성자 퍼블릭하게 부를필요없기때문에 프로텍티드
    private Article(String title, String content, String hashtag) { // 얘도 뭐 그니까 프라이베잇
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(!(obj instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }
}
