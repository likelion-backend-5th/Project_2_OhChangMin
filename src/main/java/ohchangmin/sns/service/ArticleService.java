package ohchangmin.sns.service;

import lombok.RequiredArgsConstructor;
import ohchangmin.sns.domain.Article;
import ohchangmin.sns.domain.ArticleImage;
import ohchangmin.sns.domain.User;
import ohchangmin.sns.dto.ArticleCreateRequest;
import ohchangmin.sns.exception.NotFoundArticle;
import ohchangmin.sns.repository.ArticleImageRepository;
import ohchangmin.sns.repository.ArticleRepository;
import ohchangmin.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    public void uploadArticle(Long userId, ArticleCreateRequest request) {
        User user = userRepository.findByIdOrThrow(userId);
        Article article = request.toEntity();
        user.uploadArticle(article);
    }

    public void addArticleImages(Long userId, Long articleId, List<String> imageUrls) {
        Article article = articleRepository.findByIdWithUser(articleId)
                .orElseThrow(NotFoundArticle::new);
        article.verifyUser(userId);

        List<ArticleImage> articleImages = createArticleImages(imageUrls);
        if (!existsArticleImagesBy(articleId)) {
            setThumbnail(articleImages);
        }
        article.addImages(articleImages);
    }

    private List<ArticleImage> createArticleImages(List<String> imageUrls) {
        return imageUrls.stream()
                .map(ArticleImage::new)
                .toList();
    }

    private boolean existsArticleImagesBy(Long articleId) {
        return articleImageRepository.existsByArticleId(articleId);
    }

    private void setThumbnail(List<ArticleImage> articleImages) {
        articleImages.stream()
                .findFirst()
                .ifPresent(image -> image.setThumbnail(true));
    }
}
