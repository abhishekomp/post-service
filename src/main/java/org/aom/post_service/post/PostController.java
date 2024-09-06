package org.aom.post_service.post;

import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/post-api/")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    public PostController(PostService postService) {
        logger.info("PostController constructor was invoked");
        this.postService = postService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(){
        logger.info("PostController::getAllPosts() was invoked");
        List<Post> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable @Pattern(regexp = "[0-9]+", message = "id should be all numbers") String id){
        //String not matching the pattern will result in HandlerMethodValidationException exception and this is handled in the GlobalExceptionHandler class.
        logger.info("PostController::getSinglePost() was invoked with id: {}", id);
        Post post = postService.findById(Integer.parseInt(id));
        return ResponseEntity.ok(post);
    }

    @GetMapping("/allByUserId")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@RequestParam String userId){
        logger.info("PostController::getAllPostsByUserId() was invoked");
        List<Post> allPosts = postService.getAllPostsByUserId(userId);
        logger.debug("PostController::getAllPostsByUserId() received in response {} posts", allPosts.size());
        logger.trace("PostController::getAllPostsByUserId() received in response {}", allPosts);
        return ResponseEntity.ok(allPosts);
    }

    @PostMapping("createPost")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post){
        logger.info("PostController::createPost() was invoked");
        return postService.createNewPost(post);
    }

    @PostMapping("createPostV2")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPostV2(@RequestBody Post post, UriComponentsBuilder uriComponentsBuilder){
        logger.info("PostController::createPostV2() was invoked");
        postService.createNewPost(post);
        return ResponseEntity.created(
                uriComponentsBuilder.path("/post-api/post/{username}").build(post.getId()))
                .build();
    }

    @PostMapping("createMultiplePost")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Post> createMultiplePost(@RequestBody List<Post> posts){
        logger.info("PostController::createMultiplePost() was invoked");
        logger.debug("PostController::createMultiplePost() was invoked with {}", posts);
        return postService.createMultipleNewPosts(posts);
    }

    @PutMapping("updatePost/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Post updatePost(@RequestBody Post post, @PathVariable int id){
        return postService.updatePost(post, id);
    }
}
