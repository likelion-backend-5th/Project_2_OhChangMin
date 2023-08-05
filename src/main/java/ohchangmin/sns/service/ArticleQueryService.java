package ohchangmin.sns.service;

import lombok.RequiredArgsConstructor;
import ohchangmin.sns.domain.*;
import ohchangmin.sns.dto.ArticleElementResponse;
import ohchangmin.sns.dto.ArticleResponse;
import ohchangmin.sns.exception.NotFoundArticle;
import ohchangmin.sns.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final UserFollowRepository userFollowRepository;

    public List<ArticleElementResponse> findArticles(Long userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleImage> articleImages = articleImageRepository.findInArticle(articles);

        List<ArticleElementResponse> responses = createArticleResponse(articles);
        Map<Long, String> imageUrlMap = createImageUrlMap(articleImages);
        setRepresentativeImages(responses, imageUrlMap);
        return responses;
    }

    public ArticleResponse findArticle(Long articleId) {
        Article article = articleRepository.findByIdWithUser(articleId)
                .orElseThrow(NotFoundArticle::new);

        return new ArticleResponse(article);
    }

    public List<ArticleElementResponse> findFollowArticles(Long userId) {
        List<UserFollow> userFollows = userFollowRepository.findByFollowingIdWithFollower(userId);
        List<User> followers = userFollows.stream()
                .map(UserFollow::getFollower)
                .toList();

        List<Article> articles = articleRepository.findByUserIn(followers);
        List<ArticleImage> articleImages = articleImageRepository.findInArticle(articles);

        List<ArticleElementResponse> responses = createArticleResponse(articles);
        Map<Long, String> imageUrlMap = createImageUrlMap(articleImages);
        setRepresentativeImages(responses, imageUrlMap);
        return responses;
    }

    private List<ArticleElementResponse> createArticleResponse(List<Article> articles) {
        return articles.stream()
                .map(ArticleElementResponse::new)
                .toList();
    }

    private Map<Long, String> createImageUrlMap(List<ArticleImage> articleImages) {
        return articleImages.stream()
                .collect(Collectors.toMap(ArticleImage::getArticleId, ArticleImage::getImageUrl));
    }

    private void setRepresentativeImages(List<ArticleElementResponse> articles, Map<Long, String> imageUrlMap) {
        articles.forEach(article -> {
            article.setThumbnail(imageUrlMap.get(article.getId()));
        });
    }
}
