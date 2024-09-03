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
        this.postRepository = postRepository;
    }

    List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    Post createNewPost(Post post){
        logger.debug("PostService::createNewPost() received post: {}", post);
        Post post1 = setToFalseIfIsBlockedIsMissing(post);
        return postRepository.save(post1);
    }

    List<Post> createMultipleNewPosts(List<Post> posts){
        List<Post> posts1 = posts.stream().map(post -> setToFalseIfIsBlockedIsMissing(post))
                .toList();
        return postRepository.saveAll(posts1);
    }

    private Post setToFalseIfIsBlockedIsMissing(Post post) {
        if(post.getBlocked() == null){
            post.setBlocked(false);
        }
        return post;
    }

    void deleteById(int id){
        postRepository.deleteById(id);
    }

    Post updatePost(Post post, int id){
        Post postFromDb = findById(id);
        post.setId(id);
        return postRepository.save(post);
    }

    List<Post> getPostByUserId(String userId){
        return postRepository.findByUserId(userId);
    }

    public List<Post> getAllPostsByUserId(String userId) {
        return postRepository.findAll().stream()
                .filter(post -> userId.equalsIgnoreCase(post.getUserId()))
                .toList();
    }

    public Post findById(int id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", id)));
    }
}
