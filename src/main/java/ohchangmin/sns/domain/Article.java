package ohchangmin.sns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleImage> articleImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    private String title;

    @Lob
    private String content;

    private boolean draft;

    private LocalDateTime deletedAt;

    @Builder
    private Article(String title, String content, boolean draft) {
        this.title = title;
        this.content = content;
        this.draft = draft;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void verifyUser(Long userId) {
        user.checkEquals(userId);
    }

    public void addImages(List<ArticleImage> articleImages) {
        articleImages.forEach(articleImage -> {
            this.articleImages.add(articleImage);
            articleImage.setArticle(this);
        });
    }

    public String getUsername() {
        return this.user.getUsername();
    }
}