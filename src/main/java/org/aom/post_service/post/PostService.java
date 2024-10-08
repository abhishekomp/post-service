package org.aom.post_service.post;

import org.aom.post_service.post.exception.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        logger.debug("PostService::constructor() was invoked");
        this.postRepository = postRepository;
    }

    List<Post> getAllPosts(){
        logger.info("PostService::getAllPosts() was invoked");
        return postRepository.findAll();
    }

    Post createNewPost(Post post){
        logger.info("PostService::createNewPost() was invoked");
        logger.debug("PostService::createNewPost() was invoked with post: {}", post);
        Post post1 = setToFalseIfIsBlockedIsMissing(post);
        logger.debug("PostService::createNewPost() returning post: {}", post1);
        return postRepository.save(post1);
    }

    List<Post> createMultipleNewPosts(List<Post> posts){
        logger.info("PostService::createMultipleNewPosts() was invoked");
        logger.debug("PostService::createMultipleNewPosts() was invoked with posts {}", posts);
        List<Post> posts1 = posts.stream().map(post -> setToFalseIfIsBlockedIsMissing(post))
                .toList();
        return postRepository.saveAll(posts1);
    }

    private Post setToFalseIfIsBlockedIsMissing(Post post) {
        logger.debug("PostService::setToFalseIfIsBlockedIsMissing() was invoked with post {}", post);
        if(post.getBlocked() == null){
            post.setBlocked(false);
        }
        logger.debug("PostService::setToFalseIfIsBlockedIsMissing() returning post {}", post);
        return post;
    }

//    void deleteById(int id){
//        logger.info("PostService::deleteById() was invoked");
//        logger.debug("PostService::deleteById() was invoked with id {}", id);
//        postRepository.deleteById(id);
//    }

    Post updatePost(Post post, int id){
        logger.info("PostService::updatePost() was invoked");
        logger.debug("PostService::updatePost() invoked with post: {} and id: {}", post, id);
        Post postFromDb = findById(id);
        post.setId(id);
        return postRepository.save(post);
    }

//    List<Post> getPostByUserId(String userId){
//        return postRepository.findByUserId(userId);
//    }

    public List<Post> getAllPostsByUserId(String userId) {
        logger.info("PostService::getAllPostsByUserId() was invoked");
        logger.debug("PostService::getAllPostsByUserId() invoked with userId: {}", userId);
        return postRepository.findAll().stream()
                .filter(post -> userId.equalsIgnoreCase(post.getUserId()))
                .toList();
    }

    public Post findById(int id) {
        logger.info("PostService::findById() was invoked");
        logger.debug("PostService::findById() received id: {}", id);
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", id)));
    }

    public void deletePost(int postId) {
        logger.info("PostService::deletePost() was invoked");
        logger.debug("PostService::deletePost() received id: {}", postId);
        Post byId = findById(postId);
        postRepository.deleteById(postId);
    }
}
